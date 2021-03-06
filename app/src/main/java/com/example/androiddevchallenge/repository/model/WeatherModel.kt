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
package com.example.androiddevchallenge.repository.model

import com.example.androiddevchallenge.network.models.Current
import com.example.androiddevchallenge.network.models.Daily
import com.example.androiddevchallenge.network.models.Hourly

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
