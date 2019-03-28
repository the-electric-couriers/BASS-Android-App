package com.electriccouriers.bass.models;

import android.util.Log;

import com.google.gson.annotations.SerializedName;

public class History {

    @SerializedName("routePointName")
    private String routePointName;

    @SerializedName("timestamp")
    private String timestamp;

    @SerializedName("date")
    private String date;

    @SerializedName("time")
    private String time;

    @SerializedName("status")
    private Integer status;

    @SerializedName("type")
    private Integer type;

    public History(String routePointName, String timestamp, String date, String time, Integer status, Integer type) {
        this.routePointName = routePointName;
        this.timestamp = timestamp;
        this.date = date;
        this.time = time;
        this.status = status;
        this.type = type;
    }

    public void setRoutePointName(String routePointName) {
        this.routePointName = routePointName;
    }

    public String getRoutePointName() {
        return routePointName;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDate() {
        return date;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getTime() {
        return time;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getStatus() {
        return status;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }
}