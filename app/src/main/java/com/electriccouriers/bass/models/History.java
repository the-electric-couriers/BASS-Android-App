package com.electriccouriers.bass.models;

import android.util.Log;

import com.google.gson.annotations.SerializedName;

public class History {

    @SerializedName("routePointName")
    private String routePointName;

    @SerializedName("date")
    private String date;

    @SerializedName("time")
    private String time;

    @SerializedName("status")
    private Integer status;

    @SerializedName("type")
    private Integer type;

    public History(String routePointName, String date, String time, Integer status, Integer type) {
        this.routePointName = routePointName;
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