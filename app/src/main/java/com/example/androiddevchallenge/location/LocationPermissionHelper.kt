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

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.Settings
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import com.example.androiddevchallenge.R
import java.lang.ref.WeakReference

class LocationPermissionHelper(private val activity: WeakReference<Activity>) {
    fun hasLocationPermissions(): Boolean = activity.get()?.let {
        ContextCompat.checkSelfPermission(
            it,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(
                    it,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED
    } ?: false

    fun requestPermissions(requestCode: Int) = activity.get()?.requestPermissions(
        arrayOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ),
        requestCode
    )

    fun showPermissionDeniedDialog(): Unit = activity.get()?.let {
        AlertDialog.Builder(it)
            .setMessage(it.getString(R.string.denied_permissions))
            .setCancelable(true)
            .setPositiveButton(it.getString(R.string.permission_dialog_positive_button)) { _, _ -> goToApplicationSettings() }
            .setNegativeButton(it.getString(R.string.permission_dialog_negative_button), null)
            .show()
        Unit
    } ?: Unit

    fun permissionGranted(grantResults: IntArray): Boolean =
        (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)

    /**
     * Launch Application Settings for App so user can set appropriate permissions
     */
    private fun goToApplicationSettings(): Unit = activity.get()?.let {
        val intent = Intent().apply {
            action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS

            val uri = Uri.fromParts("package", it.packageName, null)
            data = uri
        }
        it.startActivity(intent)
    } ?: Unit
}
