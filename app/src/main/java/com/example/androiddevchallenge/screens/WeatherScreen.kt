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
package com.example.androiddevchallenge.screens

import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Switch
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.androiddevchallenge.Celsius
import com.example.androiddevchallenge.Fahrenheit
import com.example.androiddevchallenge.KilometersPerHour
import com.example.androiddevchallenge.MilesPerHour
import com.example.androiddevchallenge.network.OpenWeatherAPI
import com.example.androiddevchallenge.network.models.Daily
import com.example.androiddevchallenge.screens.helper.getIconResource
import com.example.androiddevchallenge.screens.viewmodels.WeatherUIState
import com.example.androiddevchallenge.screens.viewmodels.WeatherViewModel
import com.example.androiddevchallenge.screens.widgets.WeatherTop
import dev.chrisbanes.accompanist.coil.CoilImage
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

@Composable
fun WeatherScreen(
    weatherViewModel: WeatherViewModel = viewModel(),
    retry: (String) -> Unit = {}
) {
    val uiState: WeatherUIState by weatherViewModel.currentUIState.observeAsState(WeatherUIState.Loading)

    when (uiState) {
        is WeatherUIState.Loaded -> LoadedScreen(
            state = uiState as WeatherUIState.Loaded,
            refresh = retry
        )
        is WeatherUIState.Error -> ErrorScreen {
            retry(OpenWeatherAPI.Units.IMPERIAL)
        }
        WeatherUIState.Loading -> LoadingScreen()
    }
}

@Composable
fun LoadedScreen(state: WeatherUIState.Loaded, refresh: (String) -> Unit = {}) =
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        Box(modifier = Modifier.fillMaxSize()) {

            var toggleSwitchState by remember { mutableStateOf(false) }
            val temperatureUnit = if (toggleSwitchState) Celsius else Fahrenheit
            val speedUnit = if (toggleSwitchState) KilometersPerHour else MilesPerHour

            CoilImage(
                data = state.model.backgroundPhotoUrl,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop,
                contentDescription = "${state.cityName} ${state.stateName}"
            )

            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {

                val color = MaterialTheme.colors.surface
                val backgroundColor by remember { mutableStateOf(color) }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp, end = 16.dp),
                    horizontalArrangement = Arrangement.End
                ) {

                    Text("Toggle Temp Unit ")

                    Switch(
                        checked = toggleSwitchState,
                        onCheckedChange = {
                            toggleSwitchState = it
                            if (toggleSwitchState)
                                refresh(OpenWeatherAPI.Units.METRIC)
                            else
                                refresh(OpenWeatherAPI.Units.IMPERIAL)
                        }
                    )
                }

                Crossfade(
                    targetState = backgroundColor,
                    animationSpec = tween(300)
                ) { selectedColor ->
                    WeatherTop(
                        weather = state.model,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        units = temperatureUnit,
                        speedUnit = speedUnit,
                        backgroundColor = selectedColor
                    )
                }

                Crossfade(
                    targetState = backgroundColor,
                    animationSpec = tween(300)
                ) { selectedColor ->
                    DailyWeather(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 16.dp),
                        units = temperatureUnit,
                        backgroundColor = selectedColor,
                        dailyWeather = state.model.dailyWeather,
                        timezoneOffset = state.model.timezoneOffset
                    )
                }
            }
        }
    }

@Composable
fun DailyWeather(
    modifier: Modifier,
    backgroundColor: Color,
    dailyWeather: List<Daily>,
    timezoneOffset: Int,
    units: String
) =
    LazyRow(
        modifier = modifier,
        content = {
            items(dailyWeather) { item ->
                Day(
                    item,
                    timezoneOffset,
                    backgroundColor,
                    units,
                    modifier = Modifier
                        .width(150.dp)
                        .height(225.dp)
                        .padding(end = 8.dp)
                )
            }
        }
    )

@Composable
fun Day(
    item: Daily,
    timezoneOffset: Int,
    backgroundColor: Color,
    units: String,
    modifier: Modifier
) =
    Card(modifier = modifier, backgroundColor = backgroundColor) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            val date = Date(item.dt * 1000 - (timezoneOffset * 1000)).formatTo("EEE, MMM d")
            val weather = item.weather.first()
            val icon = weather.getIconResource()

            Text(text = date, style = MaterialTheme.typography.subtitle1)
            Image(
                painter = painterResource(id = icon),
                contentDescription = weather.main
            )

            Text(text = weather.main, style = MaterialTheme.typography.body1)

            Text(text = "${item.temp.max.toInt()}° $units", style = MaterialTheme.typography.body1)
        }
    }

fun Date.formatTo(dateFormat: String, timeZone: TimeZone = TimeZone.getTimeZone("UTC")): String {
    val formatter = SimpleDateFormat(dateFormat, Locale.getDefault())
    formatter.timeZone = timeZone
    return formatter.format(this)
}
