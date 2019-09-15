package co.yulu.assignment.ui.main

import android.Manifest
import android.app.Activity
import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager.PERMISSION_GRANTED
import android.inputmethodservice.InputMethodService
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
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
import co.yulu.assignment.ui.main.adapter.NearbyPlacesAdapter
import co.yulu.assignment.ui.main.adapter.QueryResultAdapter
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

    private val BENGALURU_LAT_LNG = LatLng(12.972227, 77.593961)

    private lateinit var currentLoction: Location

    @Inject
    internal lateinit var viewModelProviderFactory: ViewModelProviderFactory

    @BindView(R.id.nearbyPlacesRV)
    lateinit var nearbyPlacesRV: RecyclerView

    private val mainViewModel: MainViewModel
        get() {
            return ViewModelProviders.of(this, viewModelProviderFactory).get(MainViewModel::class.java)
        }

    private lateinit var mMap: GoogleMap

    private lateinit var locationUtil: LocationUtil

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        ButterKnife.bind(this)
        if (PermissionUtil.isPermissionGranted(this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION))) {
            initMap()
        } else {
            PermissionUtil.requestPermissions(this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION),
                LOCATION_PERMISSION_REQUEST_CODE)
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
                mMap.clear()
                hideKeyboard()
                mainViewModel.getNearbyPlaces("${currentLoction.latitude},${currentLoction.longitude}", query ?: "")
                mainViewModel.getLiveData().observe(this@MainActivity, Observer {
                    when(it.status) {
                        Status.LOADING -> {
                        }
                        Status.SUCCESS -> {
                            populateQueryResultOnMaps(it.data!!)
                            populateQueryDataToList(it.data)
                        }
                        Status.ERROR -> {
                        }
                    }
                })
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }

        })

        return super.onCreateOptionsMenu(menu);
    }

    private fun initMap() {
        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment!!.getMapAsync(this)

        locationUtil = object: LocationUtil() {

            override fun onLocationChanged(location: Location?) {
                super.onLocationChanged(location)
                currentLoction = location!!
                //mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(LatLng(location!!.latitude, location.longitude), 15.2f))
                mainViewModel.exploreNearbyPlaces("${location.latitude},${location.longitude}")
                observeLiveData()
                stopReceivingLocation()
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

    private fun observeLiveData() {
        mainViewModel.getNearbyPlacesLiveData().observe(this, Observer {
            when(it.status) {
                Status.LOADING -> {
                    Log.d(TAG, "loading")
                }

                Status.SUCCESS -> {
                    Log.d(TAG, "success:${it.data}")
                    populateOnMaps(it.data!!.items)
                    populateDataToList(it.data.items)
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

    private fun populateDataToList(data: List<Item>?) {
        val adapter = NearbyPlacesAdapter(data!!, this@MainActivity)

        nearbyPlacesRV.layoutManager = LinearLayoutManager(this)

        nearbyPlacesRV.adapter = adapter
    }

    private fun populateQueryDataToList(data: List<Venue>?) {
        val adapter = QueryResultAdapter(data!!, this@MainActivity)

        nearbyPlacesRV.layoutManager = LinearLayoutManager(this)

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
                initMap()
            } else {
                UIUtil.showShortToast(this@MainActivity, "Location could not be traced, please try again.")
            }
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
                initMap()
            } else {
                UIUtil.showShortToast(this@MainActivity, "Location Permission is not granted, please allow it from Settings")
                locationUtil.switchOnGPSAndStartReceivingLocation(this@MainActivity)
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }
}
