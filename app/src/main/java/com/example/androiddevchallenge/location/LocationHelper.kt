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
package com.example.androiddevchallenge.location

import android.annotation.SuppressLint
import android.location.Location
import android.os.Looper
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult

class LocationHelper(
    private var fusedLocationClient: FusedLocationProviderClient,
) : LifecycleObserver {

    private lateinit var locationRequest: LocationRequest
    private lateinit var locationCallback: LocationCallback

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun onStop() {
        if (::locationRequest.isInitialized && ::locationCallback.isInitialized) {
            fusedLocationClient.removeLocationUpdates(locationCallback)
        }
    }

    /**
     * Get current location for user.
     * Supressing lint rule because we are doing the permission check.
     */
    @SuppressLint("MissingPermission")
    fun getCurrentLocation(updateLocation: (Location) -> Unit) {

        createLocationRequest()
        createLocationCallBack(updateLocation)

        fusedLocationClient.removeLocationUpdates(locationCallback)

        fusedLocationClient.requestLocationUpdates(
            locationRequest,
            locationCallback,
            Looper.getMainLooper()
        )

        fusedLocationClient.lastLocation
            .addOnSuccessListener { location: Location? ->
                location?.let {
                    updateLocation(it)
                }
            }
    }

    private fun createLocationRequest() {
        locationRequest = LocationRequest.create().apply {
            interval = 60000.toLong() * 10 // update every 10 minutes
            fastestInterval = 50.toLong()
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }
    }

    private fun createLocationCallBack(updateLocation: (Location) -> Unit) {
        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                super.onLocationResult(locationResult)
                for (location in locationResult.locations) {
                    updateLocation(location)
                }
            }
        }
    }
}
