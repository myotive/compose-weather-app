/*
 * Copyright 2021 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.androiddevchallenge.repository

import com.example.androiddevchallenge.di.IoDispatcher
import com.example.androiddevchallenge.network.OpenWeatherAPI
import com.example.androiddevchallenge.network.UnsplashAPI
import com.example.androiddevchallenge.repository.model.WeatherModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject

interface IWeatherRepository {
    suspend fun fetchWeather(
        lat: Double,
        long: Double,
        cityName: String,
        stateName: String,
        currentUnit: String = OpenWeatherAPI.Units.IMPERIAL
    ): WeatherModel
}

class WeatherRepository @Inject constructor(
    private val openWeatherAPI: OpenWeatherAPI,
    private val unsplashAPI: UnsplashAPI,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : IWeatherRepository {

    override suspend fun fetchWeather(
        lat: Double,
        long: Double,
        cityName: String,
        stateName: String,
        currentUnit: String
    ): WeatherModel = withContext(ioDispatcher) {

        Timber.i("Fetch weather data for $lat $long")
        val openWeatherData = openWeatherAPI.getCurrentWeather(lat, long, currentUnit)
        Timber.i(openWeatherData.toString())

        Timber.i("Fetch something from unsplash for $cityName, $stateName")
        val unsplashData = unsplashAPI.search("$cityName, $stateName")

        // choose random photo from result
        val randomPhoto = unsplashData.results.first {
            it.urls.raw.isNotEmpty()
        }
        Timber.i(randomPhoto.toString())

        WeatherModel(
            backgroundPhotoUrl = randomPhoto.urls.raw,
            currentWeather = openWeatherData.current,
            dailyWeather = openWeatherData.daily,
            hourlyWeather = openWeatherData.hourly,
            timezoneOffset = openWeatherData.timezoneOffset,
            city = cityName,
            state = stateName
        )
    }
}
