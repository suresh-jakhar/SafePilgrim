package com.safepilgrim.digitalid.dto;

import java.util.List;

public class GenerateIdRequest {
    private PassportData passportData;
    private List<EmergencyContact> emergencyContacts;
    private TravelItinerary travelItinerary;
    private List<KycDocument> kycDocuments;
    private BiometricData biometricData;

    // Constructors
    public GenerateIdRequest() {}

    public GenerateIdRequest(PassportData passportData, List<EmergencyContact> emergencyContacts,
                            TravelItinerary travelItinerary, List<KycDocument> kycDocuments,
                            BiometricData biometricData) {
        this.passportData = passportData;
        this.emergencyContacts = emergencyContacts;
        this.travelItinerary = travelItinerary;
        this.kycDocuments = kycDocuments;
        this.biometricData = biometricData;
    }

    // Getters and Setters
    public PassportData getPassportData() { return passportData; }
    public void setPassportData(PassportData passportData) { this.passportData = passportData; }

    public List<EmergencyContact> getEmergencyContacts() { return emergencyContacts; }
    public void setEmergencyContacts(List<EmergencyContact> emergencyContacts) { this.emergencyContacts = emergencyContacts; }

    public TravelItinerary getTravelItinerary() { return travelItinerary; }
    public void setTravelItinerary(TravelItinerary travelItinerary) { this.travelItinerary = travelItinerary; }

    public List<KycDocument> getKycDocuments() { return kycDocuments; }
    public void setKycDocuments(List<KycDocument> kycDocuments) { this.kycDocuments = kycDocuments; }

    public BiometricData getBiometricData() { return biometricData; }
    public void setBiometricData(BiometricData biometricData) { this.biometricData = biometricData; }
}
