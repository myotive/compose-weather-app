package com.example.androiddevchallenge.repository.model

import com.example.androiddevchallenge.network.models.Current
import com.example.androiddevchallenge.network.models.Daily
import com.example.androiddevchallenge.network.models.Hourly

data class WeatherModel(
    val backgroundPhotoUrl: String,
    val currentWeather: Current,
    val hourlyWeather: List<Hourly>,
    val dailyWeather: List<Daily>,
)