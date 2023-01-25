package com.udacity.asteroidradar.api

import com.squareup.moshi.Moshi.*
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.udacity.asteroidradar.Constants
import com.udacity.asteroidradar.PictureOfDay
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import java.util.*
import java.util.concurrent.TimeUnit

private val moshi = Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

interface Service
{

    //to use inline data use const data following way
//    @GET("neo/rest/v1/feed?start_date=$START_DATE&end_date=$END_DATE&api_key=${Constants.API_KEY}")
//    fun getAsteroids(): Call<String>

    //for adding non-static data use @Query following way
    @GET("neo/rest/v1/feed?")
    fun getAsteroids(@Query("start_date") start_date: String, @Query("end_date") end_date: String,@Query("api_key") api_key: String): Call<String>

    //to use inline data use const data following way
    @GET("planetary/apod?api_key=${Constants.API_KEY}")
    fun getPictureOfTheDay() : Call<PictureOfDay>

}

/**
 * Main entry point for network access. Call like `Network.devbytes.getPlaylist()`
 */
object Network {

    var client = OkHttpClient.Builder()
        .connectTimeout(100, TimeUnit.SECONDS)
        .readTimeout(100, TimeUnit.SECONDS).build()
    // Configure retrofit to parse JSON and use coroutines
    private val retrofit = Retrofit.Builder()
        .baseUrl(Constants.BASE_URL).client(client)
        .addConverterFactory(ScalarsConverterFactory.create())
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .build()

    val api = retrofit.create(Service::class.java)
}