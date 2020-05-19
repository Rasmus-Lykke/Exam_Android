package com.example.my_parking.model;

import android.location.Location;

public class Favorite {

    private String title;
    private String comment;
    private String id;
    private String latitude;
    private String longitude;

    public Favorite(String title, String comment, String id, String latitude, String longitude) {
        this.title = title;
        this.comment = comment;
        this.id = id;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLatitude() {
        return latitude;
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

    public double setLongitude(String longitude) {
        if (latitude.length() > 0) {
            return Double.parseDouble(latitude);
        }
        return 0;
    }
}
