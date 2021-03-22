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
        if(::locationRequest.isInitialized && ::locationCallback.isInitialized) {
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