package com.electriccouriers.bass.models;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

public class User {

    @SerializedName("userID")
    private Integer userID;

    @SerializedName("email")
    private String email;

    @SerializedName("token")
    private String token;

    @SerializedName("admin")
    private Integer admin;

    @SerializedName("firstname")
    private String firstname;

    @SerializedName("lastname")
    private String lastname;

    @SerializedName("companyID")
    private Integer companyID;

    @SerializedName("name")
    private String companyName;

    @SerializedName("routePointID")
    private Integer routePointID;

    @SerializedName("accessCode")
    private String accessCode;

    private Company company;

    public User(Integer userID, String email, String token, Integer admin, String firstname, String lastname, Integer companyID, String companyName, Integer routePointID, String accessCode) {
        this.userID = userID;
        this.email = email;
        this.token = token;
        this.admin = admin;
        this.firstname = firstname;
        this.lastname = lastname;

        this.companyID = companyID;
        this.companyName = companyName;
        this.routePointID = routePointID;
        this.accessCode = accessCode;

        this.company = new Company(companyID, companyName, routePointID);
    }

    public Integer getUserID() {
        return userID;
    }

    public void setUserID(Integer userID) {
        this.userID = userID;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Integer getAdmin() {
        return admin;
    }

    public void setAdmin(Integer admin) {
        this.admin = admin;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getFullName() {
        return this.firstname + " " + this.lastname;
    }

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public String getAccessCode() {
        return accessCode;
    }

    public void setAccessCode(String accessCode) {
        this.accessCode = accessCode;
    }


    public String serialize() {
        Gson gson = new Gson();
        return gson.toJson(this);
    }

    static public User create(String serializedData) {
        Gson gson = new Gson();
        return gson.fromJson(serializedData, User.class);
    }
}