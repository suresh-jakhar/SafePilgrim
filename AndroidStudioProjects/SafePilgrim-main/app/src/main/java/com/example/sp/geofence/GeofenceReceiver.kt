package com.example.sp.geofence

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.google.android.gms.location.Geofence
import com.google.android.gms.location.GeofencingEvent

class GeofenceReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val event = GeofencingEvent.fromIntent(intent) ?: return
        val transition = event.geofenceTransition
        val ids = event.triggeringGeofences?.joinToString { it.requestId } ?: ""
        val mgr = GeofenceManager(context)
        when (transition) {
            Geofence.GEOFENCE_TRANSITION_ENTER -> mgr.notify("Geofence", "Entered: $ids")
            Geofence.GEOFENCE_TRANSITION_EXIT -> mgr.notify("Geofence", "Exited: $ids")
        }
    }
}


