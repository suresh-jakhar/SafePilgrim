package com.safepilgrim.digitalid.dto;

public class BiometricData {
    private String fingerprintTemplate;
    private String faceTemplate;
    private String voiceTemplate;

    public BiometricData() {}

    public BiometricData(String fingerprintTemplate, String faceTemplate, String voiceTemplate) {
        this.fingerprintTemplate = fingerprintTemplate;
        this.faceTemplate = faceTemplate;
        this.voiceTemplate = voiceTemplate;
    }

    public String getFingerprintTemplate() { return fingerprintTemplate; }
    public void setFingerprintTemplate(String fingerprintTemplate) { this.fingerprintTemplate = fingerprintTemplate; }

    public String getFaceTemplate() { return faceTemplate; }
    public void setFaceTemplate(String faceTemplate) { this.faceTemplate = faceTemplate; }

    public String getVoiceTemplate() { return voiceTemplate; }
    public void setVoiceTemplate(String voiceTemplate) { this.voiceTemplate = voiceTemplate; }
}
