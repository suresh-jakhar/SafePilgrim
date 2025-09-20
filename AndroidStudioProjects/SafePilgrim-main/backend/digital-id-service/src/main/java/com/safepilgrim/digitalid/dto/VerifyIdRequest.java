package com.safepilgrim.digitalid.dto;

public class VerifyIdRequest {
    private String digitalId;
    private BiometricData biometricData;
    private LocationData location;

    public VerifyIdRequest() {}

    public VerifyIdRequest(String digitalId, BiometricData biometricData, LocationData location) {
        this.digitalId = digitalId;
        this.biometricData = biometricData;
        this.location = location;
    }

    public String getDigitalId() { return digitalId; }
    public void setDigitalId(String digitalId) { this.digitalId = digitalId; }

    public BiometricData getBiometricData() { return biometricData; }
    public void setBiometricData(BiometricData biometricData) { this.biometricData = biometricData; }

    public LocationData getLocation() { return location; }
    public void setLocation(LocationData location) { this.location = location; }
}
