package com.electriccouriers.bass.helpers;

import com.electriccouriers.bass.data.Globals;
import com.electriccouriers.bass.interfaces.APIService;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class API {

    private static <T> T builder(Class<T> endpoint) {
        return new Retrofit.Builder()
                .baseUrl(Globals.API_BASEURL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(endpoint);
    }

    public static APIService service() {
        return builder(APIService.class);
    }
}