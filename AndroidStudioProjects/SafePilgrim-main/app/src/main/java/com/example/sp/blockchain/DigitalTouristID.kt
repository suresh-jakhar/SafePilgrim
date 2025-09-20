package com.example.sp.blockchain

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDateTime

@Entity(tableName = "digital_tourist_ids")
data class DigitalTouristID(
    @PrimaryKey
    val id: String,
    val passportNumber: String,
    val name: String,
    val nationality: String,
    val entryDate: LocalDateTime,
    val exitDate: LocalDateTime,
    val emergencyContacts: List<EmergencyContact>,
    val itinerary: List<ItineraryItem>,
    val qrCode: String,
    val blockchainHash: String,
    val isActive: Boolean = true,
    val createdAt: LocalDateTime = LocalDateTime.now(),
    val updatedAt: LocalDateTime = LocalDateTime.now()
)

data class EmergencyContact(
    val name: String,
    val relationship: String,
    val phoneNumber: String,
    val email: String
)

data class ItineraryItem(
    val id: String,
    val location: String,
    val plannedDate: LocalDateTime,
    val actualDate: LocalDateTime? = null,
    val status: ItineraryStatus = ItineraryStatus.PLANNED
)

enum class ItineraryStatus {
    PLANNED, IN_PROGRESS, COMPLETED, CANCELLED, DELAYED
}

data class TouristRegistrationData(
    val passportNumber: String,
    val name: String,
    val nationality: String,
    val entryDate: LocalDateTime,
    val exitDate: LocalDateTime,
    val emergencyContacts: List<EmergencyContact>,
    val itinerary: List<ItineraryItem>,
    val biometricData: BiometricData? = null
)

data class BiometricData(
    val fingerprintHash: String? = null,
    val faceIdHash: String? = null,
    val voicePrintHash: String? = null
)
