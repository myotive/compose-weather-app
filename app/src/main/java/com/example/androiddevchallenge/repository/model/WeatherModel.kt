package com.example.androiddevchallenge.repository.model

import com.example.androiddevchallenge.network.models.Current
import com.example.androiddevchallenge.network.models.Daily
import com.example.androiddevchallenge.network.models.Hourly
import com.example.androiddevchallenge.network.models.Weather

data class WeatherModel(
    val backgroundPhotoUrl: String = "",
    val city: String,
    val state: String,
    val timezoneOffset: Int,
    val currentWeather: Current = Current(),
    val hourlyWeather: List<Hourly> = listOf(),
    val dailyWeather: List<Daily> = listOf(),
) {
    val formattedCityState
        get() = "$city, $state"
}