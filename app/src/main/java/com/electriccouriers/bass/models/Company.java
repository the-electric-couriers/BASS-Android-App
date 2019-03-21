package com.electriccouriers.bass.models;

public class Company {

    private Integer companyID;
    private String name;
    private Integer routePointID;

    public Company(Integer companyID, String name, Integer routePointID) {
        this.companyID = companyID;
        this.name = name;
        this.routePointID = routePointID;
    }

    public Integer getCompanyID() {
        return companyID;
    }

    public void setCompanyID(Integer companyID) {
        this.companyID = companyID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getRoutePointID() {
        return routePointID;
    }

    public void setRoutePointID(Integer routePointID) {
        this.routePointID = routePointID;
    }
}
