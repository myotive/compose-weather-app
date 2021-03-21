package com.example.androiddevchallenge.network

import com.example.androiddevchallenge.BuildConfig
import com.example.androiddevchallenge.network.models.OpenWeatherOneCallResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface OpenWeatherAPI {
    @GET("onecall")
    suspend fun getCurrentWeather(
        @Query("lat") lat: Double,
        @Query("lon") long: Double,
        @Query("appid") appid: String = BuildConfig.OPEN_WEATHER_MAPS_API_KEY
    ): OpenWeatherOneCallResponse
}