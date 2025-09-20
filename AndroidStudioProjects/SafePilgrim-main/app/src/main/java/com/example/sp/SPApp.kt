package com.example.sp

import android.app.Application
import com.example.sp.data.AppContainer

class SPApp : Application() {
    lateinit var container: AppContainer
        private set

    override fun onCreate() {
        super.onCreate()
        container = AppContainer(this)
    }
}


