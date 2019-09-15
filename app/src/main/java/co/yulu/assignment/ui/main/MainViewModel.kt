package co.yulu.assignment.ui.main

import androidx.lifecycle.MutableLiveData
import co.barfi.network.handler.Resource
import co.yulu.assignment.application.base.BaseViewModel
import co.yulu.assignment.network.responsehandlers.NearbyPlacesResult
import co.yulu.assignment.database.entity.Venue
import co.yulu.assignment.network.responsehandlers.suggestedplaces.Group
import com.google.gson.JsonObject
import javax.inject.Inject

class MainViewModel @Inject
internal constructor() : BaseViewModel() {

    @Inject
    lateinit var mainRepository: MainRepository

    private var searchResultsLiveData = MutableLiveData<Resource<List<Venue>>>()
    private var nearbyPlacesLiveData = MutableLiveData<Resource<Group>>()

    internal fun getSearchResultsLiveData() = searchResultsLiveData
    internal fun getNearbyPlacesLiveData() = nearbyPlacesLiveData

    internal fun getSearchResults(latLngString: String, queryText: String) {
        mainRepository.getNearbyPlaces(latLngString, queryText).observeForever{
                userResource -> searchResultsLiveData.postValue(userResource)
        }
    }

    internal fun exploreNearbyPlaces(latLngString: String) {
        mainRepository.exploreNearbyPlaces(latLngString).observeForever {
            nearbyPlacesLiveData.postValue(it)
        }
    }
}