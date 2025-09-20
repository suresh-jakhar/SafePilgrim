package com.safepilgrim.digitalid.dto;

public class GenerateIdResponse {
    private String digitalId;
    private String qrCode;
    private String blockchainHash;
    private String status;
    private String message;

    public GenerateIdResponse() {}

    public GenerateIdResponse(String digitalId, String qrCode, String blockchainHash, String status, String message) {
        this.digitalId = digitalId;
        this.qrCode = qrCode;
        this.blockchainHash = blockchainHash;
        this.status = status;
        this.message = message;
    }

    // Getters and Setters
    public String getDigitalId() { return digitalId; }
    public void setDigitalId(String digitalId) { this.digitalId = digitalId; }

    public String getQrCode() { return qrCode; }
    public void setQrCode(String qrCode) { this.qrCode = qrCode; }

    public String getBlockchainHash() { return blockchainHash; }
    public void setBlockchainHash(String blockchainHash) { this.blockchainHash = blockchainHash; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
}
