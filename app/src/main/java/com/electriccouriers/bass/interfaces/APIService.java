package com.electriccouriers.bass.interfaces;

import com.electriccouriers.bass.models.History;
import com.electriccouriers.bass.models.RoutePoint;
import com.electriccouriers.bass.models.User;

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
    Call<List<RoutePoint>> routePoints(@Header("Authorization") String auth);

    @POST("user/history")
    @FormUrlEncoded
    Call<ArrayList<History>> routeHistory(@Header("Authorization") String auth, @Field("userID") Integer userID);
}