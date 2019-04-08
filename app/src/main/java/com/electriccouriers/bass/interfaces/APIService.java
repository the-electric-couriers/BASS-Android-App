package com.electriccouriers.bass.interfaces;

import com.electriccouriers.bass.models.History;
import com.electriccouriers.bass.models.RoutePoint;
import com.electriccouriers.bass.models.User;
import com.google.gson.JsonElement;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface APIService {

    @POST("user/login")
    @FormUrlEncoded
    Call<User> login(@Field("email") String email, @Field("password") String password);

    @POST("route/points")
    Call<ArrayList<RoutePoint>> routePoints(@Header("Authorization") String auth);

    @POST("user/history")
    @FormUrlEncoded
    Call<ArrayList<History>> routeHistory(@Header("Authorization") String auth, @Field("userID") Integer userID);

    @POST("route/new")
    @FormUrlEncoded
    Call<JsonElement> requestRide(@Header("Authorization") String auth, @Field("userID") Integer userID, @Field("startPosition") Integer startPosition, @Field("endPosition") Integer endPosition);

    @POST("route/checkIn")
    @FormUrlEncoded
    Call<JsonElement> checkIn(@Header("Authorization") String auth, @Field("userID") Integer userID, @Field("card") String cardID, @Field("routeID") Integer routeID);
}