package com.safepilgrim.digitalid.dto;

public class EmergencyContact {
    private String name;
    private String relationship;
    private String phoneNumber;
    private String email;

    public EmergencyContact() {}

    public EmergencyContact(String name, String relationship, String phoneNumber, String email) {
        this.name = name;
        this.relationship = relationship;
        this.phoneNumber = phoneNumber;
        this.email = email;
    }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getRelationship() { return relationship; }
    public void setRelationship(String relationship) { this.relationship = relationship; }

    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
}
