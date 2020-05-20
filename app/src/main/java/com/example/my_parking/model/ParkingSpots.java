package com.example.my_parking.model;

public class ParkingSpots {

    private String id;
    private String title;
    private String description;
    private String latitude;
    private String longitude;

    public ParkingSpots(String id, String title, String description, String latitude, String longitude) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.latitude = latitude;
        this.longitude = longitude;
    }



    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public double getLatitude() {
        if (latitude.length() > 0) {
            return Double.parseDouble(latitude);
        }
        return 0;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        if (longitude.length() > 0) {
            return Double.parseDouble(longitude);
        }
        return 0;
    }

    public void setLongitude(String longitude) { this.longitude = longitude; }
}
