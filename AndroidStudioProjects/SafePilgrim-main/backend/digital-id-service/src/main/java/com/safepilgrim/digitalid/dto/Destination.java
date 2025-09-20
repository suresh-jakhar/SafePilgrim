package com.safepilgrim.digitalid.dto;

public class Destination {
    private String id;
    private String name;
    private LocationData coordinates;
    private String plannedDate;

    public Destination() {}

    public Destination(String id, String name, LocationData coordinates, String plannedDate) {
        this.id = id;
        this.name = name;
        this.coordinates = coordinates;
        this.plannedDate = plannedDate;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public LocationData getCoordinates() { return coordinates; }
    public void setCoordinates(LocationData coordinates) { this.coordinates = coordinates; }

    public String getPlannedDate() { return plannedDate; }
    public void setPlannedDate(String plannedDate) { this.plannedDate = plannedDate; }
}
