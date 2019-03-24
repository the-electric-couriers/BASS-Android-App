package com.electriccouriers.bass.models;

import com.google.gson.annotations.SerializedName;

public class RoutePoint {

    @SerializedName("routePointID")
    private Integer ID;

    @SerializedName("pointName")
    private String name;

    @SerializedName("lat")
    private Double lat;

    @SerializedName("lon")
    private Double lon;

    public RoutePoint(Integer ID, String name, Double lat, Double lon) {
        this.ID = ID;
        this.name = name;
        this.lat = lat;
        this.lon = lon;
    }

    public Integer getID() {
        return ID;
    }

    public void setID(Integer ID) {
        this.ID = ID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public Double getLon() {
        return lon;
    }

    public void setLon(Double lon) {
        this.lon = lon;
    }
}
