package com.example.sp.api.models

data class LocationData(
    val latitude: Double,
    val longitude: Double,
    val accuracy: Float,
    val altitude: Double? = null,
    val speed: Float? = null,
    val bearing: Float? = null,
    val provider: String? = null
)


data class EmergencyContact(
    val name: String,
    val relationship: String,
    val phoneNumber: String,
    val email: String?
)


data class DeviceInfo(
    val deviceId: String,
    val platform: String = "Android",
    val appVersion: String,
    val batteryLevel: Int,
    val networkStatus: String
)


data class BehaviorData(
    val movementSpeed: String, // e.g., "walking", "running", "stationary"
    val travelMode: String,    // e.g., "foot", "vehicle", "public_transport"
    val recentActivity: List<String> // e.g., ["entered_restricted_zone", "panic_button_pressed"]
)


data class ContextData(
    val weatherCondition: String, // e.g., "sunny", "rainy", "foggy"
    val timeOfDay: String,        // e.g., "day", "night", "dawn", "dusk"
    val localEventDensity: String, // e.g., "low", "medium", "high"
    val publicTransportStatus: String // e.g., "normal", "disrupted"
)

enum class TimeWindow {
    LAST_HOUR, LAST_6_HOURS, LAST_24_HOURS, LAST_7_DAYS
}


data class AlertSettings(
    val enterAlert: Boolean,
    val exitAlert: Boolean,
    val dwellAlert: Boolean,
    val notificationMessage: String
)


data class ItineraryItem(
    val id: String,
    val location: String,
    val plannedDate: String
)

enum class EmergencyType {
    MEDICAL, SECURITY, NATURAL_DISASTER, ACCIDENT, OTHER
}