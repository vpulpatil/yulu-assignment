package co.yulu.assignment.network.apiservices

import androidx.lifecycle.LiveData
import co.yulu.assignment.network.handler.ApiResponse
import com.google.gson.JsonObject
import retrofit2.http.*

interface PlacesApiService {

    @FormUrlEncoded
    @GET("/v2/venues/search")
    fun searchVenues(@Query("ll") latlng: String, @Query("intent") intent: String)
            : LiveData<ApiResponse<JsonObject>>

}
