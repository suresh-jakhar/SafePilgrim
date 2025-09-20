package com.safepilgrim.digitalid.dto;

public class VerifyIdResponse {
    private boolean isValid;
    private String verificationLevel;
    private String message;
    private long timestamp;

    public VerifyIdResponse() {}

    public VerifyIdResponse(boolean isValid, String verificationLevel, String message, long timestamp) {
        this.isValid = isValid;
        this.verificationLevel = verificationLevel;
        this.message = message;
        this.timestamp = timestamp;
    }

    public boolean isValid() { return isValid; }
    public void setValid(boolean valid) { isValid = valid; }

    public String getVerificationLevel() { return verificationLevel; }
    public void setVerificationLevel(String verificationLevel) { this.verificationLevel = verificationLevel; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public long getTimestamp() { return timestamp; }
    public void setTimestamp(long timestamp) { this.timestamp = timestamp; }
}
