package com.example.sp.api.models.requests

import com.example.sp.api.models.EmergencyContact

data class GenerateIdRequest(
    val passportData: PassportData,
    val emergencyContacts: List<EmergencyContact>,
    val travelItinerary: TravelItinerary,
    val kycDocuments: List<KycDocument>,
    val biometricData: BiometricData?
)

data class PassportData(
    val passportNumber: String,
    val name: String,
    val nationality: String,
    val dateOfBirth: String,
    val expiryDate: String
)

data class TravelItinerary(
    val entryDate: String,
    val exitDate: String,
    val plannedDestinations: List<Destination>
)

data class Destination(
    val id: String,
    val location: String,
    val plannedDate: String
)

data class KycDocument(
    val type: String,
    val documentData: String, // Base64 encoded
    val mimeType: String
)

data class BiometricData(
    val fingerprintData: String?, // Base64 encoded
    val faceData: String?, // Base64 encoded
    val voiceData: String? // Base64 encoded
)
