package com.safepilgrim.digitalid.dto;

public class KycDocument {
    private String type;
    private String documentNumber;
    private String issueDate;
    private String expiryDate;
    private String documentImage;

    public KycDocument() {}

    public KycDocument(String type, String documentNumber, String issueDate, String expiryDate, String documentImage) {
        this.type = type;
        this.documentNumber = documentNumber;
        this.issueDate = issueDate;
        this.expiryDate = expiryDate;
        this.documentImage = documentImage;
    }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public String getDocumentNumber() { return documentNumber; }
    public void setDocumentNumber(String documentNumber) { this.documentNumber = documentNumber; }

    public String getIssueDate() { return issueDate; }
    public void setIssueDate(String issueDate) { this.issueDate = issueDate; }

    public String getExpiryDate() { return expiryDate; }
    public void setExpiryDate(String expiryDate) { this.expiryDate = expiryDate; }

    public String getDocumentImage() { return documentImage; }
    public void setDocumentImage(String documentImage) { this.documentImage = documentImage; }
}
