package com.example.androiddevchallenge.screens.widgets

import androidx.compose.ui.graphics.Color
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.androiddevchallenge.repository.model.WeatherModel
import com.example.androiddevchallenge.screens.helper.getIconResource
import kotlin.math.roundToInt

@Composable
fun WeatherTop(
    weather: WeatherModel,
    backgroundColor: Color,
    modifier: Modifier,
    units: String,
    speedUnit: String
) =
    Column(
        modifier = modifier.background(color = backgroundColor),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceEvenly
    ) {

        val currWeather = weather.currentWeather.weather.first()

        Text(
            text = weather.formattedCityState,
            modifier = Modifier.padding(24.dp),
            style = MaterialTheme.typography.h5,
            color = MaterialTheme.colors.onSurface
        )

        Text(
            text = "${weather.currentWeather.temp.roundToInt()}° $units",
            style = MaterialTheme.typography.h1,
            color = MaterialTheme.colors.onSurface
        )

        Image(
            painter = painterResource(id = currWeather.getIconResource()),
            contentDescription = currWeather.main
        )

        Text(
            text = currWeather.main,
            style = MaterialTheme.typography.body1,
            color = MaterialTheme.colors.onSurface
        )

        Text(
            text = "Feels like: ${weather.currentWeather.feelsLike.roundToInt()}° $units",
            style = MaterialTheme.typography.subtitle1,
            color = MaterialTheme.colors.onSurface
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                modifier = Modifier.padding(start = 5.dp, end = 5.dp),
                text = "Humidity: ${weather.currentWeather.humidity}%",
                style = MaterialTheme.typography.body1,
                color = MaterialTheme.colors.onSurface
            )
            Text(
                modifier = Modifier.padding(start = 5.dp, end = 5.dp),
                text = "Wind: ${weather.currentWeather.windSpeed.toInt()} $speedUnit",
                style = MaterialTheme.typography.body1,
                color = MaterialTheme.colors.onSurface
            )
        }
    }