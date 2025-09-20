package com.example.sp.location

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

class LocationService(private val context: Context) {

    private val client by lazy { LocationServices.getFusedLocationProviderClient(context) }

    @SuppressLint("MissingPermission")
    fun locationUpdates(intervalMs: Long = 30_000L): Flow<Location> = callbackFlow {
        val request = com.google.android.gms.location.LocationRequest.Builder(intervalMs)
            .setMinUpdateIntervalMillis(intervalMs)
            .setPriority(Priority.PRIORITY_BALANCED_POWER_ACCURACY)
            .build()

        val callback = object : com.google.android.gms.location.LocationCallback() {
            override fun onLocationResult(result: com.google.android.gms.location.LocationResult) {
                for (loc in result.locations) trySend(loc)
            }
        }

        client.requestLocationUpdates(request, callback, context.mainLooper)
        awaitClose { client.removeLocationUpdates(callback) }
    }
}


