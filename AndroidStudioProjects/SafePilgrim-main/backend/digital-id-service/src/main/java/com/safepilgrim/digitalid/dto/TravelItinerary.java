package com.safepilgrim.digitalid.dto;

import java.util.List;

public class TravelItinerary {
    private String entryDate;
    private String exitDate;
    private List<Destination> plannedDestinations;

    public TravelItinerary() {}

    public TravelItinerary(String entryDate, String exitDate, List<Destination> plannedDestinations) {
        this.entryDate = entryDate;
        this.exitDate = exitDate;
        this.plannedDestinations = plannedDestinations;
    }

    public String getEntryDate() { return entryDate; }
    public void setEntryDate(String entryDate) { this.entryDate = entryDate; }

    public String getExitDate() { return exitDate; }
    public void setExitDate(String exitDate) { this.exitDate = exitDate; }

    public List<Destination> getPlannedDestinations() { return plannedDestinations; }
    public void setPlannedDestinations(List<Destination> plannedDestinations) { this.plannedDestinations = plannedDestinations; }
}
