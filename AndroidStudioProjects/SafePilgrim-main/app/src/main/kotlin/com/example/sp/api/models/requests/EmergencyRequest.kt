package com.example.sp.api.models.requests

import com.example.sp.api.models.DeviceInfo
import com.example.sp.api.models.EmergencyType
import com.example.sp.api.models.LocationData

data class EmergencyAlertRequest(
    val userId: String,
    val location: LocationData,
    val emergencyType: String,
    val audioData: String?, // Base64 encoded
    val userMessage: String?,
    val timestamp: Long,
    val deviceInfo: DeviceInfo
)

