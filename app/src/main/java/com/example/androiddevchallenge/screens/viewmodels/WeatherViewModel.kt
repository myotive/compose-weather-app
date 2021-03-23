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
package com.example.androiddevchallenge.screens.viewmodels

import android.content.Context
import android.location.Address
import android.location.Geocoder
import android.location.Location
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.androiddevchallenge.di.IoDispatcher
import com.example.androiddevchallenge.di.MainDispatcher
import com.example.androiddevchallenge.network.OpenWeatherAPI
import com.example.androiddevchallenge.repository.IWeatherRepository
import com.example.androiddevchallenge.repository.model.WeatherModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Locale
import javax.inject.Inject

sealed class WeatherUIState() {
    object Loading : WeatherUIState()
    class Loaded(val model: WeatherModel, val cityName: String, val stateName: String) :
        WeatherUIState()

    class Error(val error: Throwable) : WeatherUIState()
}

@HiltViewModel
class WeatherViewModel @Inject constructor(
    private val weatherRepository: IWeatherRepository,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
    @MainDispatcher private val mainDispatcher: CoroutineDispatcher
) : ViewModel() {

    private var _uiState: MutableLiveData<WeatherUIState> = MutableLiveData(WeatherUIState.Loading)
    val currentUIState: LiveData<WeatherUIState> = _uiState

    @Suppress("BlockingMethodInNonBlockingContext")
    fun load(
        context: Context,
        location: Location,
        currentUnit: String = OpenWeatherAPI.Units.IMPERIAL
    ) = viewModelScope.launch {
        withContext(ioDispatcher) {
            val geocoder = Geocoder(context, Locale.getDefault())
            val addresses: List<Address> =
                geocoder.getFromLocation(location.latitude, location.longitude, 1)
            val address = addresses.getOrElse(0) { Address(Locale.getDefault()) }

            val cityName: String = address.locality
            val stateName: String = address.adminArea

            val model = weatherRepository.fetchWeather(
                location.latitude,
                location.longitude,
                cityName,
                stateName,
                currentUnit
            )

            // Update UI State to be Loaded
            withContext(mainDispatcher) {
                _uiState.value = WeatherUIState.Loaded(model, cityName, stateName)
            }
        }
    }
}
