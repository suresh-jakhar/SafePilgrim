package com.example.sp.data

import android.app.Application
import com.example.sp.location.LocationService

class AppContainer(application: Application) {
    val locationService: LocationService = LocationService(application.applicationContext)
}


