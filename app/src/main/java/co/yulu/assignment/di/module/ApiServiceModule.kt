package co.yulu.assignment.di.module

import co.yulu.assignment.network.apiservices.PlacesApiService
import javax.inject.Singleton

import dagger.Module
import dagger.Provides
import retrofit2.Retrofit

@Module
class ApiServiceModule {

    @Provides
    @Singleton
    internal fun providePlacesApiService(retrofit: Retrofit): PlacesApiService {
        return retrofit.create(PlacesApiService::class.java)
    }

}
