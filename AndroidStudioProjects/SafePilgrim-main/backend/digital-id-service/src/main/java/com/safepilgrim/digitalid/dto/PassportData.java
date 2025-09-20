package com.safepilgrim.digitalid.dto;

public class PassportData {
    private String passportNumber;
    private String name;
    private String nationality;
    private String dateOfBirth;
    private String expiryDate;

    public PassportData() {}

    public PassportData(String passportNumber, String name, String nationality, String dateOfBirth, String expiryDate) {
        this.passportNumber = passportNumber;
        this.name = name;
        this.nationality = nationality;
        this.dateOfBirth = dateOfBirth;
        this.expiryDate = expiryDate;
    }

    public String getPassportNumber() { return passportNumber; }
    public void setPassportNumber(String passportNumber) { this.passportNumber = passportNumber; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getNationality() { return nationality; }
    public void setNationality(String nationality) { this.nationality = nationality; }

    public String getDateOfBirth() { return dateOfBirth; }
    public void setDateOfBirth(String dateOfBirth) { this.dateOfBirth = dateOfBirth; }

    public String getExpiryDate() { return expiryDate; }
    public void setExpiryDate(String expiryDate) { this.expiryDate = expiryDate; }
}
