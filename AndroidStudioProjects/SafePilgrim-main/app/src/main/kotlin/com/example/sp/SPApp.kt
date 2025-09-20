package com.example.sp

import android.app.Application
import androidx.work.*
import com.example.sp.api.workers.ApiHealthCheckWorker
import java.util.concurrent.TimeUnit

// Temporarily disabled Hilt until API layer is restored
class SPApp : Application() {

    override fun onCreate() {
        super.onCreate()

        // Initialize API health monitoring
        startApiHealthMonitoring()
    }

    private fun startApiHealthMonitoring() {
        // Background health checks every 15 minutes
        val healthCheckRequest = PeriodicWorkRequestBuilder<ApiHealthCheckWorker>(15, TimeUnit.MINUTES)
            .setConstraints(
                Constraints.Builder()
                    .setRequiredNetworkType(NetworkType.CONNECTED)
                    .build()
            )
            .build()

        WorkManager.getInstance(this).enqueueUniquePeriodicWork(
            ApiHealthCheckWorker.WORK_NAME,
            ExistingPeriodicWorkPolicy.KEEP,
            healthCheckRequest
        )

        android.util.Log.d("SPApp", "API health monitoring started")
    }
}
