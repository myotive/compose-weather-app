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

import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.androiddevchallenge.location.LocationHelper
import com.example.androiddevchallenge.location.PermissionHelper
import com.example.androiddevchallenge.screens.WeatherScreen
import com.example.androiddevchallenge.screens.viewmodels.WeatherViewModel
import com.example.androiddevchallenge.ui.theme.MyTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

private const val LOCATION_ACCESS_REQUEST_CODE = 1001

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var locationHelper: LocationHelper
    @Inject
    lateinit var permissionHelper: PermissionHelper

    private val weatherViewModel: WeatherViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        lifecycle.addObserver(locationHelper)

        when {
            permissionHelper.hasLocationPermissions() -> locationHelper.getCurrentLocation {
                updateLocation(it)
            }
            else -> permissionHelper.requestPermissions(LOCATION_ACCESS_REQUEST_CODE)
        }

        setContent {
            MyTheme {
                WeatherScreen(weatherViewModel) {
                    locationHelper.getCurrentLocation {
                        updateLocation(it)
                    }
                }
            }
        }
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        when (requestCode) {
            LOCATION_ACCESS_REQUEST_CODE -> {
                if (permissionHelper.permissionGranted(grantResults)) {
                    locationHelper.getCurrentLocation {
                        updateLocation(it)
                    }
                }
            }
            else -> {
                if (!permissionHelper.hasLocationPermissions()) {
                    // Permission is not granted (Permanently)
                    permissionHelper.showPermissionDeniedDialog()
                }
            }
        }
    }

    private fun updateLocation(location: Location) = weatherViewModel.load(this, location)
}