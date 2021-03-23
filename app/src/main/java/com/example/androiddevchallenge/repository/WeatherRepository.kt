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