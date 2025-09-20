package com.safepilgrim.digitalid.dto;

public class UpdateIdResponse {
    private boolean success;
    private String newBlockchainHash;
    private String message;

    public UpdateIdResponse() {}

    public UpdateIdResponse(boolean success, String newBlockchainHash, String message) {
        this.success = success;
        this.newBlockchainHash = newBlockchainHash;
        this.message = message;
    }

    public boolean isSuccess() { return success; }
    public void setSuccess(boolean success) { this.success = success; }

    public String getNewBlockchainHash() { return newBlockchainHash; }
    public void setNewBlockchainHash(String newBlockchainHash) { this.newBlockchainHash = newBlockchainHash; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
}
