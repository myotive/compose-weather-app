package com.example.androiddevchallenge.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Button
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.androiddevchallenge.repository.model.WeatherModel
import com.example.androiddevchallenge.screens.viewmodels.WeatherUIState
import com.example.androiddevchallenge.screens.viewmodels.WeatherViewModel
import dev.chrisbanes.accompanist.coil.CoilImage

@Composable
fun WeatherScreen(
    weatherViewModel: WeatherViewModel = viewModel(),
    retry: () -> Unit = {}
) {
    val uiState: WeatherUIState by weatherViewModel.currentUIState.observeAsState(WeatherUIState.Loading)

    when (uiState) {
        is WeatherUIState.Loaded -> LoadedScreen(state = uiState as WeatherUIState.Loaded)
        is WeatherUIState.Error -> ErrorScreen() {
            retry()
        }
        WeatherUIState.Loading -> LoadingScreen()
    }
}

@Composable
fun LoadedScreen(state: WeatherUIState.Loaded) = Box(modifier = Modifier.fillMaxSize()) {
    CoilImage(
        data = state.model.backgroundPhotoUrl,
        contentDescription = "Background image"
    )

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Hey, we did it.")
        Text(text = "${state.cityName}, ${state.stateName}")
    }
}

@Composable
fun LoadingScreen() = Column(
    modifier = Modifier.fillMaxSize(),
    verticalArrangement = Arrangement.Center,
    horizontalAlignment = Alignment.CenterHorizontally
) {
    CircularProgressIndicator()
}

@Composable
fun ErrorScreen(tryAgainClicked: () -> Unit = {}) = Column(
    modifier = Modifier.fillMaxSize(),
    verticalArrangement = Arrangement.Center,
    horizontalAlignment = Alignment.CenterHorizontally
) {
    Text(text = "There was an issue loading this screen.")
    Button(onClick = { tryAgainClicked() }) {
        Text(text = "Try again?")
    }
}
