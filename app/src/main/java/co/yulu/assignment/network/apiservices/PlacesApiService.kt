package co.yulu.assignment.network.apiservices

import androidx.lifecycle.LiveData
import co.yulu.assignment.network.handler.ApiResponse
import co.yulu.assignment.network.responsehandlers.NearbyPlacesResult
import co.yulu.assignment.network.responsehandlers.Venue
import co.yulu.assignment.network.responsehandlers.suggestedplaces.SuggestedPlaces
import com.google.gson.JsonObject
import retrofit2.http.*

interface PlacesApiService {

    @GET("/v2/venues/search")
    fun searchVenues(
        @Query("client_id") clientId: String,
        @Query("client_secret") clientSecret: String,
        @Query("v") clientVersion: String,
        @Query("ll") latlng: String)
            : LiveData<ApiResponse<NearbyPlacesResult>>

    @GET("/v2/venues/explore")
    fun explorePlaces(
        @Query("client_id") clientId: String,
        @Query("client_secret") clientSecret: String,
        @Query("v") clientVersion: String,
        @Query("ll") latlng: String)
            : LiveData<ApiResponse<SuggestedPlaces>>

}
