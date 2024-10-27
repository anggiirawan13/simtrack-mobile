package com.simple.tracking

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

class LocationPermissionHelper(private val activity: Activity) {

    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1001
    }

    fun checkLocationPermission(onPermissionGranted: () -> Unit) {
        if (ContextCompat.checkSelfPermission(
                activity,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // Permission is not granted; request it
            requestLocationPermission()
        } else {
            // Permission is already granted
            onPermissionGranted()
        }
    }

    private fun requestLocationPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(
                activity,
                Manifest.permission.ACCESS_FINE_LOCATION
            )
        ) {
            // Show an explanation to the user why the permission is needed
            Toast.makeText(activity, "Location permission is required for this feature", Toast.LENGTH_SHORT).show()
        }

        // Request the permission
        ActivityCompat.requestPermissions(
            activity,
            arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
            LOCATION_PERMISSION_REQUEST_CODE
        )
    }

    fun handlePermissionResult(
        requestCode: Int,
        grantResults: IntArray,
        onPermissionGranted: () -> Unit
    ) {
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted
                onPermissionGranted()
            } else {
                // Permission denied; prompt again
                Toast.makeText(activity, "Location permission is necessary. Please allow it.", Toast.LENGTH_SHORT).show()
                requestLocationPermission()
            }
        }
    }
}
