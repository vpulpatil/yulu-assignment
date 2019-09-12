package co.yulu.assignment.di.module

import co.yulu.assignment.di.DiConstants
import co.yulu.assignment.network.handler.LiveDataCallAdapterFactory
import co.yulu.assignment.util.creds.ClientCreds
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Named
import javax.inject.Singleton

@Module
class AppModule {

    @Provides
    @Singleton
    internal fun providesGson(): Gson = GsonBuilder().create()

    @Provides
    @Named(DiConstants.API_END_POINT)
    internal fun providesBaseUrl(): String = "https://api.foursquare.com"


    @Provides
    @Singleton
    internal fun providesClientCreds(): ClientCreds=
        ClientCreds("VESL1AI5ST5GPRTMCCZWMUYVB0MC3R3O4RKSTR2BUHCKFCTD",
            "MJBNMC333AHJHXTOIW25GFIYF1Q0GFSWD0AQ3BJHS4FRAUNM", "20180323")

    @Provides
    @Singleton
    internal fun provideRetrofit(gson: Gson, @Named(DiConstants.API_END_POINT) BASE_URL: String
    ): Retrofit = Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create(gson))
        .addCallAdapterFactory(LiveDataCallAdapterFactory())
        .baseUrl(BASE_URL)
        .build()

}
