package co.yulu.assignment.ui.main

import android.Manifest
import android.app.Activity
import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager.PERMISSION_GRANTED
import android.location.Location
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.Menu
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.widget.SearchView
import androidx.core.app.ActivityCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import butterknife.BindView
import butterknife.ButterKnife
import co.yulu.assignment.R
import co.yulu.assignment.application.base.BaseActivity
import co.yulu.assignment.di.ViewModelProviderFactory
import co.yulu.assignment.network.handler.Status
import co.yulu.assignment.database.entity.Venue
import co.yulu.assignment.network.responsehandlers.suggestedplaces.Item
import co.yulu.assignment.ui.main.adapter.PlacesAdapter
import co.yulu.assignment.util.UIUtil
import co.yulu.assignment.util.location.LocationUtil
import co.yulu.assignment.util.permission.LOCATION_PERMISSION_REQUEST_CODE
import co.yulu.assignment.util.permission.PermissionUtil
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLngBounds
import javax.inject.Inject
import com.google.android.gms.maps.model.MarkerOptions

class MainActivity : BaseActivity(), OnMapReadyCallback {

    private val TAG = "MainActivity"

    val GETTING_LOCATION_STATE = 2001
    val GETTING_NEARBY_PLACES_STATE = 2002
    val NO_DATA_FOUND_STATE = 2003
    val SHOW_DATA_STATE = 2004
    val ERROR_OCCURRED_STATE = 2005
    val SEARCHING_PLACES_STATE = 2006

    val SETTING_REQUEST_CODE = 1234

    private val BENGALURU_LAT_LNG = LatLng(12.972227, 77.593961)

    private lateinit var currentLoction: Location

    @Inject
    internal lateinit var viewModelProviderFactory: ViewModelProviderFactory

    @BindView(R.id.nearbyPlacesRV)
    lateinit var nearbyPlacesRV: RecyclerView

    @BindView(R.id.progressLayoutLL)
    lateinit var progressLayoutLL: LinearLayout

    @BindView(R.id.progressTextTV)
    lateinit var progressTextTV: TextView

    @BindView(R.id.errorTV)
    lateinit var errorTV: TextView

    private val mainViewModel: MainViewModel
        get() {
            return ViewModelProviders.of(this, viewModelProviderFactory).get(MainViewModel::class.java)
        }

    private lateinit var mMap: GoogleMap

    private lateinit var locationUtil: LocationUtil

    private var isObservingSearchResultsData = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        ButterKnife.bind(this)
        initView()
    }

    private fun checkLocationPermission() {
        if (PermissionUtil.isPermissionGranted(this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION))) {
            getCurrentLocation()
        } else {
            requestLocationPermission()
        }
    }

    private fun requestLocationPermission() {
        PermissionUtil.requestPermissions(this,
            arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION),
            LOCATION_PERMISSION_REQUEST_CODE)
    }

    private fun handleStates(state: Int) {
        when(state) {
            GETTING_LOCATION_STATE -> {
                progressLayoutLL.visibility = View.VISIBLE
                progressTextTV.setText(R.string.getting_location_text)
                nearbyPlacesRV.visibility = View.INVISIBLE
                errorTV.visibility = View.GONE
            }
            GETTING_NEARBY_PLACES_STATE -> {
                progressLayoutLL.visibility = View.VISIBLE
                progressTextTV.setText(R.string.getting_nearby_places_text)
                nearbyPlacesRV.visibility = View.INVISIBLE
                errorTV.visibility = View.GONE
            }
            SEARCHING_PLACES_STATE -> {
                progressLayoutLL.visibility = View.VISIBLE
                progressTextTV.setText(R.string.searching_nearby_places_text)
                nearbyPlacesRV.visibility = View.INVISIBLE
                errorTV.visibility = View.GONE
            }
            NO_DATA_FOUND_STATE -> {
                progressLayoutLL.visibility = View.GONE
                nearbyPlacesRV.visibility = View.INVISIBLE
                errorTV.visibility = View.VISIBLE
                errorTV.setText(R.string.no_data_found_text)
            }
            SHOW_DATA_STATE -> {
                progressLayoutLL.visibility = View.GONE
                errorTV.visibility = View.GONE
                nearbyPlacesRV.visibility = View.VISIBLE
            }
            ERROR_OCCURRED_STATE -> {
                progressLayoutLL.visibility = View.GONE
                nearbyPlacesRV.visibility = View.INVISIBLE
                errorTV.visibility = View.VISIBLE
                errorTV.setText(R.string.error_occurred_text)
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val menuInflater = menuInflater
        menuInflater.inflate(R.menu.main_menu, menu)

        val searchItem = menu.findItem(R.id.action_search)

        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager

        val searchView = searchItem.actionView as SearchView
        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))

        searchView.setOnQueryTextListener(object: SearchView.OnQueryTextListener{

            override fun onQueryTextSubmit(query: String?): Boolean {
                Log.d(TAG, "onQueryTextSubmit")
                mMap.clear()
                hideKeyboard()
                mainViewModel.getSearchResults("${currentLoction.latitude},${currentLoction.longitude}", query ?: "")
                observeSearchResultsLiveData()
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                Log.d(TAG, "onQueryTextChange")
                return false
            }

        })

        return super.onCreateOptionsMenu(menu);
    }

    private fun observeSearchResultsLiveData() {
        if (!isObservingSearchResultsData) {
            isObservingSearchResultsData = true
            mainViewModel.getSearchResultsLiveData()
                .observe(this@MainActivity, Observer { filteredVenues ->
                    when (filteredVenues.status) {
                        Status.LOADING -> {
                            handleStates(SEARCHING_PLACES_STATE)
                        }
                        Status.SUCCESS -> {
                            if (filteredVenues.data!!.isNotEmpty()) {
                                handleStates(SHOW_DATA_STATE)
                                populateQueryResultOnMaps(filteredVenues.data)
                                setAdapter(filteredVenues.data)
                            } else {
                                handleStates(NO_DATA_FOUND_STATE)
                            }
                        }
                        Status.ERROR -> {
                            handleStates(ERROR_OCCURRED_STATE)
                        }
                    }
                })
        }
    }

    private fun initView() {
        handleStates(GETTING_LOCATION_STATE)
        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment!!.getMapAsync(this)

        val llm = LinearLayoutManager(this)
        nearbyPlacesRV.layoutManager = llm
        val itemDecor = DividerItemDecoration(this@MainActivity, llm.orientation);
        nearbyPlacesRV.addItemDecoration(itemDecor)

        checkLocationPermission()
    }

    private fun getCurrentLocation() {
        if (!this::locationUtil.isInitialized) {
            locationUtil = object : LocationUtil() {

                override fun onLocationChanged(location: Location?) {
                    super.onLocationChanged(location)
                    if (location != null) {
                        Log.d(TAG, "onLocationChanged")
                        handleStates(GETTING_NEARBY_PLACES_STATE)
                        currentLoction = location
                        //mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(LatLng(location!!.latitude, location.longitude), 15.2f))
                        mainViewModel.exploreNearbyPlaces("${location.latitude},${location.longitude}")
                        observeNearbyPlacesLiveData()
                        stopReceivingLocation()
                    }
                }

            }
        }
        locationUtil.switchOnGPSAndStartReceivingLocation(this@MainActivity)
    }

    private fun hideKeyboard() {
        val imm = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        //Find the currently focused view, so we can grab the correct window token from it.
        var view = currentFocus
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = View(this)
        }
        imm.hideSoftInputFromWindow(view.windowToken, 0);
    }

    private fun observeNearbyPlacesLiveData() {
        mainViewModel.getNearbyPlacesLiveData().observe(this, Observer {
            when(it.status) {
                Status.LOADING -> {
                    Log.d(TAG, "loading")
                }

                Status.SUCCESS -> {
                    handleStates(SHOW_DATA_STATE)
                    Log.d(TAG, "success:${it.data}")
                    populateOnMaps(it.data!!.items)
                    val venues = arrayListOf<Venue>()
                    for (item in it.data.items) {
                        venues.add(item.venue)
                    }
                    setAdapter(venues)
                }

                Status.ERROR -> {
                    Log.d(TAG, "errormessage:${it.message}")
                }
            }
        })
    }

    private fun populateOnMaps(items: List<Item>) {
        mMap.clear()
        if (items.isNotEmpty()) {
            val latLngBoundsBuilder = LatLngBounds.builder()
            for (item in items) {
                val latLng = LatLng(item.venue.location!!.lat, item.venue.location!!.lng)
                latLngBoundsBuilder.include(latLng)
                createMarkerOnMap(latLng, item.venue.name!!)
            }

            //animate camera to accommodate all locations in the map
            val padding = 34 * resources.displayMetrics.density
            mMap.animateCamera(
                CameraUpdateFactory.newLatLngBounds(
                    latLngBoundsBuilder.build(),
                    padding.toInt()
                )
            )
        } else {
            UIUtil.showShortToast(this@MainActivity, "No Results with the query")
        }
    }

    private fun populateQueryResultOnMaps(items: List<Venue>) {
        mMap.clear()
        if (items.isNotEmpty()) {
            val latLngBoundsBuilder = LatLngBounds.builder()
            for (item in items) {
                val latLng = LatLng(item.location!!.lat, item.location!!.lng)
                latLngBoundsBuilder.include(latLng)
                createMarkerOnMap(latLng, item.name!!)
            }

            //animate camera to accommodate all locations in the map
            val padding = 34 * resources.displayMetrics.density
            mMap.animateCamera(
                CameraUpdateFactory.newLatLngBounds(
                    latLngBoundsBuilder.build(),
                    padding.toInt()
                )
            )
        } else {
            UIUtil.showShortToast(this@MainActivity, "No Results with the query")
        }
    }

    private fun createMarkerOnMap(latLng: LatLng, placeName: String) {
        // Creating a marker
        val markerOptions = MarkerOptions()

        // Setting the position for the marker
        markerOptions.position(latLng)

        markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.map_marker))

        // Setting the title for the marker.
        // This will be displayed on taping the marker
        markerOptions.title(placeName)

        mMap.addMarker(markerOptions)
    }

    private fun setAdapter(data: List<Venue>) {
        val adapter = PlacesAdapter(data, this@MainActivity)
        nearbyPlacesRV.adapter = adapter
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(BENGALURU_LAT_LNG, 14f))
        mMap.isMyLocationEnabled = true
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == LocationUtil.REQUEST_LOCATION) {
            if (resultCode == Activity.RESULT_OK) {
                getCurrentLocation()
            } else {
                UIUtil.showShortToast(this@MainActivity, "Location could not be traced, please try again.")
            }
        } else if (requestCode == SETTING_REQUEST_CODE) {
            checkLocationPermission()
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.size > 1 && grantResults[0] == PERMISSION_GRANTED) {
                //location permission is given
                getCurrentLocation()
            } else {
                val showRationale = ActivityCompat.
                    shouldShowRequestPermissionRationale(this@MainActivity, permissions[0]);
                if (!showRationale) {
                    // user also CHECKED "never ask again"
                    // you can either enable some fall back,
                    // disable features of your app
                    // or open another dialog explaining
                    // again the permission and directing to
                    // the app setting
                    UIUtil.showShortToast(this@MainActivity, "Location Permission is not granted, please allow it from Settings")
                    redirectToAppSettings()
                } else {
                    requestLocationPermission()
                }
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    private fun redirectToAppSettings() {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        val uri = Uri.fromParts("package", packageName, null)
        intent.data = uri
        startActivityForResult(intent, SETTING_REQUEST_CODE)
    }
}
