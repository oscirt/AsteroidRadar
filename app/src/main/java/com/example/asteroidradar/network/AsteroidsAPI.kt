package com.example.asteroidradar.network

import com.example.asteroidradar.models.PictureOfTheDay
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

private const val BASE_URL = "https://api.nasa.gov/"
private const val API_KEY = "iom5DXBVZbee8RVj16E2wQMhwhlB3YIsT9g7Vfxa"

private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

private val retrofit = Retrofit.Builder()
    .baseUrl(BASE_URL)
    .addConverterFactory(ScalarsConverterFactory.create())
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .build()

interface AsteroidsInterface {
    @GET("neo/rest/v1/feed")
    suspend fun getAsteroidsList(@Query("start_date") startDate: String,
                                 @Query("end_date") endDate: String,
                                 @Query("api_key") key: String = API_KEY
    ) : String

    @GET("planetary/apod")
    suspend fun getPictureOfTheDay(@Query("api_key") key: String = API_KEY) : PictureOfTheDay
}

object AsteroidsAPI {
    val service: AsteroidsInterface by lazy {
        retrofit.create(AsteroidsInterface::class.java)
    }
}