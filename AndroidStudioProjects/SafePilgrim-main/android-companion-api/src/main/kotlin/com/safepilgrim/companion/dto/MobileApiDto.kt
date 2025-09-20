package com.safepilgrim.companion.dto

data class OfflineDataSync(
    val userId: String,
    val data: Map<String, Any>,
    val timestamp: Long
)

data class SyncResult(
    val success: Boolean,
    val syncedItems: Int,
    val failedItems: Int,
    val message: String?
)

data class MobileConfig(
    val apiEndpoints: Map<String, String>,
    val features: Map<String, Boolean>,
    val settings: Map<String, String>
)

data class DeviceRegistration(
    val deviceId: String,
    val deviceName: String,
    val osVersion: String,
    val appVersion: String,
    val pushToken: String?
)

data class RegistrationResult(
    val success: Boolean,
    val userId: String?,
    val message: String?
)
