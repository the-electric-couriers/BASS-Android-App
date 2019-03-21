package com.electriccouriers.bass.interfaces;

import com.electriccouriers.bass.models.User;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface APIService {

    @POST("user/login")
    @FormUrlEncoded
    Call<User> login(@Field("email") String email, @Field("password") String password);
}