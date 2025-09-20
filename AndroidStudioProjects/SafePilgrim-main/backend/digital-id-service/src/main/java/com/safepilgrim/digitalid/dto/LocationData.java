package com.safepilgrim.digitalid.dto;

public class LocationData {
    private double latitude;
    private double longitude;
    private float accuracy;
    private Double altitude;
    private Float speed;
    private Float bearing;
    private String provider;

    public LocationData() {}

    public LocationData(double latitude, double longitude, float accuracy, Double altitude, Float speed, Float bearing, String provider) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.accuracy = accuracy;
        this.altitude = altitude;
        this.speed = speed;
        this.bearing = bearing;
        this.provider = provider;
    }

    public double getLatitude() { return latitude; }
    public void setLatitude(double latitude) { this.latitude = latitude; }

    public double getLongitude() { return longitude; }
    public void setLongitude(double longitude) { this.longitude = longitude; }

    public float getAccuracy() { return accuracy; }
    public void setAccuracy(float accuracy) { this.accuracy = accuracy; }

    public Double getAltitude() { return altitude; }
    public void setAltitude(Double altitude) { this.altitude = altitude; }

    public Float getSpeed() { return speed; }
    public void setSpeed(Float speed) { this.speed = speed; }

    public Float getBearing() { return bearing; }
    public void setBearing(Float bearing) { this.bearing = bearing; }

    public String getProvider() { return provider; }
    public void setProvider(String provider) { this.provider = provider; }
}
