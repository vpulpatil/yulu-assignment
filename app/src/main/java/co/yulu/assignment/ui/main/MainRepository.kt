package co.yulu.assignment.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import co.barfi.network.handler.Resource
import co.yulu.assignment.application.AppExecutors
import co.yulu.assignment.database.dao.VenueDao
import co.yulu.assignment.network.apiservices.PlacesApiService
import co.yulu.assignment.network.handler.ApiResponse
import co.yulu.assignment.network.handler.NetworkBoundResource
import co.yulu.assignment.network.responsehandlers.nearbyplaces.NearbyPlacesResult
import co.yulu.assignment.database.entity.Venue
import co.yulu.assignment.network.responsehandlers.suggestedplaces.Group
import co.yulu.assignment.network.responsehandlers.suggestedplaces.SuggestedPlaces
import co.yulu.assignment.util.creds.ClientCreds
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MainRepository @Inject
constructor() {

    @Inject
    lateinit var placesApiService: PlacesApiService

    @Inject
    lateinit var venueDao: VenueDao

    @Inject
    lateinit var clientCredentials: ClientCreds

    @Inject
    lateinit var appExecutors: AppExecutors

    fun getNearbyPlaces(latLngString: String, queryText: String): LiveData<Resource<List<Venue>>> {
        return object : NetworkBoundResource<List<Venue>, NearbyPlacesResult>(appExecutors) {
            override fun parseNetworkResponse(body: NearbyPlacesResult): MutableLiveData<List<Venue>> {
                val apiResponse = MutableLiveData<List<Venue>>()
                apiResponse.postValue(body.response.venues)
                return apiResponse
            }

            override fun saveCallResult(item: NearbyPlacesResult) {
                val venues = item.response.venues
                venueDao.insertVenues(venues)
            }

            override fun loadFromDb(): LiveData<List<Venue>>? {
                //todo: load from DB
                return null
            }

            override fun createCall(): LiveData<ApiResponse<NearbyPlacesResult>> {
                return placesApiService.searchVenues(clientCredentials.clientId,
                    clientCredentials.clientSecret, clientCredentials.clientVersion, latLngString, queryText)
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
                val items = item.response.groups[0].items
                for (singleItem in items) {
                    venueDao.insertVenue(singleItem.venue)
                }
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
