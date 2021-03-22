package com.example.androiddevchallenge.location

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.Settings
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import java.lang.ref.WeakReference

class PermissionHelper(private val activity: WeakReference<Activity>) {
    fun hasLocationPermissions(): Boolean = activity.get()?.let {
        ContextCompat.checkSelfPermission(
            it,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(
            it,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    } ?: false


    fun requestPermissions(requestCode: Int) = activity.get()?.let {
        it.requestPermissions(
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ), requestCode
        )
    }

    fun showPermissionDeniedDialog(): Unit = activity.get()?.let {
        AlertDialog.Builder(it)
            .setMessage("You have denied permanently these permissions, please go to setting to enable these permissions.")
            .setCancelable(true)
            .setPositiveButton("Go to Settings") { _, _ -> goToApplicationSettings() }
            .setNegativeButton("Cancel", null)
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