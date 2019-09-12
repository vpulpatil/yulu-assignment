package co.yulu.assignment.ui.main

import androidx.lifecycle.MutableLiveData
import co.barfi.network.handler.Resource
import co.yulu.assignment.application.base.BaseViewModel
import co.yulu.assignment.network.responsehandlers.NearbyPlacesResult
import co.yulu.assignment.network.responsehandlers.Venue
import com.google.gson.JsonObject
import javax.inject.Inject

class MainViewModel @Inject
internal constructor() : BaseViewModel() {

    @Inject
    lateinit var mainRepository: MainRepository

    private var searchResultsLiveData = MutableLiveData<Resource<List<Venue>>>()

    internal fun getLiveData() = searchResultsLiveData

    internal fun getNearbyPlaces(latLngString: String) {
        mainRepository.getNearbyPlaces(latLngString).observeForever{
                userResource -> searchResultsLiveData.postValue(userResource)
        }
    }
}