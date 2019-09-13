package co.yulu.assignment.ui.main

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import co.barfi.network.handler.Resource
import co.yulu.assignment.application.AppExecutors
import co.yulu.assignment.network.apiservices.PlacesApiService
import co.yulu.assignment.network.handler.ApiResponse
import co.yulu.assignment.network.handler.NetworkBoundResource
import co.yulu.assignment.network.responsehandlers.NearbyPlacesResult
import co.yulu.assignment.network.responsehandlers.Venue
import co.yulu.assignment.network.responsehandlers.suggestedplaces.Group
import co.yulu.assignment.network.responsehandlers.suggestedplaces.SuggestedPlaces
import co.yulu.assignment.util.creds.ClientCreds
import com.google.gson.JsonObject
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MainRepository @Inject
constructor() {

    @Inject
    lateinit var placesApiService: PlacesApiService

    @Inject
    lateinit var clientCredentials: ClientCreds

    @Inject
    lateinit var appExecutors: AppExecutors

    fun getNearbyPlaces(latLngString: String): LiveData<Resource<List<Venue>>> {
        return object : NetworkBoundResource<List<Venue>, NearbyPlacesResult>(appExecutors) {
            override fun parseNetworkResponse(body: NearbyPlacesResult): MutableLiveData<List<Venue>> {
                val apiResponse = MutableLiveData<List<Venue>>()
                apiResponse.postValue(body.response.venues)
                return apiResponse
            }

            override fun saveCallResult(item: NearbyPlacesResult) {
                //todo: save data to DB
            }

            override fun loadFromDb(): LiveData<List<Venue>>? {
                //todo: load from DB
                return null
            }

            override fun createCall(): LiveData<ApiResponse<NearbyPlacesResult>> {
                return placesApiService.searchVenues(clientCredentials.clientId,
                    clientCredentials.clientSecret, clientCredentials.clientVersion, latLngString)
            }

            override fun shouldfetchDataFromDbBeforeNetwork(): Boolean {
                return false
            }
        }.asLiveData
    }

    fun exploreNearbyPlaces(latLngString: String): LiveData<Resource<Group>> {
        return object : NetworkBoundResource<Group, SuggestedPlaces>(appExecutors) {
            override fun parseNetworkResponse(body: SuggestedPlaces): MutableLiveData<Group> {
                val apiResponse = MutableLiveData<Group>()
                apiResponse.postValue(body.response.groups[0])
                return apiResponse
            }

            override fun saveCallResult(item: SuggestedPlaces) {
                //todo: save data to DB
            }

            override fun loadFromDb(): LiveData<Group>? {
                //todo: load from DB
                return null
            }

            override fun createCall(): LiveData<ApiResponse<SuggestedPlaces>> {
                return placesApiService.explorePlaces(clientCredentials.clientId,
                    clientCredentials.clientSecret, clientCredentials.clientVersion, latLngString)
            }

            override fun shouldfetchDataFromDbBeforeNetwork(): Boolean {
                return false
            }
        }.asLiveData
    }

}
