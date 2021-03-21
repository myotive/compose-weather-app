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
package com.example.androiddevchallenge

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import androidx.activity.compose.setContent
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.example.androiddevchallenge.network.OpenWeatherAPI
import com.example.androiddevchallenge.network.UnsplashAPI
import com.example.androiddevchallenge.ui.theme.MyTheme
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.util.Locale
import javax.inject.Inject

private const val LOCATION_ACCESS_REQUEST_CODE = 1001

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var unsplashAPI: UnsplashAPI

    @Inject
    lateinit var openWeatherAPI: OpenWeatherAPI

    private lateinit var fusedLocationClient: FusedLocationProviderClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        when {
            hasLocationPermissions() -> {
                getCurrentLocation()
            }
            else -> {
                requestPermissions()
            }
        }

        setContent {
            MyTheme {
                MyApp()
            }
        }
    }

    private fun requestPermissions() = requestPermissions(
        arrayOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ), LOCATION_ACCESS_REQUEST_CODE
    )

    private fun hasLocationPermissions(): Boolean =
        ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        when (requestCode) {
            LOCATION_ACCESS_REQUEST_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    getCurrentLocation()
                }
            }
            else -> {
                if (!hasLocationPermissions()) {
                    // Permission is not granted (Permanently)
                    showPermissionDeniedDialog()
                }
            }
        }
    }


    private fun showPermissionDeniedDialog() = AlertDialog.Builder(this)
        .setMessage("You have denied permanently these permissions, please go to setting to enable these permissions.")
        .setCancelable(true)
        .setPositiveButton("Go to Settings") { dialogInterface, i -> goToApplicationSettings() }
        .setNegativeButton("Cancel", null)
        .show()


    private fun goToApplicationSettings() {
        val intent = Intent()
        intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
        val uri = Uri.fromParts("package", packageName, null)
        intent.data = uri
        startActivity(intent)
    }

    /**
     * Get current location for user.
     * Supressing lint rule because we are doing the permission check.
     */
    @SuppressLint("MissingPermission")
    private fun getCurrentLocation() = fusedLocationClient.lastLocation
        .addOnSuccessListener { location: Location? ->
            // Got last known location. In some rare situations this can be null.
            location?.let {
                val geocoder = Geocoder(this, Locale.getDefault())
                val addresses: List<Address> =
                    geocoder.getFromLocation(it.latitude, it.latitude, 1)
                val address = addresses.getOrElse(0) { Address(Locale.getDefault()) }

                val cityName: String = address.getAddressLine(0) ?: "Columbus"
                val stateName: String = address.getAddressLine(1) ?: "Ohio"

                searchForWeather(it.latitude, it.longitude, cityName, stateName)
            }
        }

    private fun searchForWeather(lat: Double, long: Double, cityName: String, stateName: String) {
        lifecycleScope.launch {
            withContext(Dispatchers.IO) {
                val openWeatherData = openWeatherAPI.getCurrentWeather(lat, long)

                Timber.i("Successful call to open weather")
                Timber.d(openWeatherData.toString())
                val unsplashData = unsplashAPI.search("$cityName, $stateName")

                Timber.i("Successful call to unsplash")
                Timber.d(unsplashData.toString())
            }
        }
    }
}

// Start building your app here!
@Composable
fun MyApp() {
    Surface(color = MaterialTheme.colors.background) {
        Text(text = "Ready... Set... GO!")
    }
}

@Preview("Light Theme", widthDp = 360, heightDp = 640)
@Composable
fun LightPreview() {
    MyTheme {
        MyApp()
    }
}

@Preview("Dark Theme", widthDp = 360, heightDp = 640)
@Composable
fun DarkPreview() {
    MyTheme(darkTheme = true) {
        MyApp()
    }
}
