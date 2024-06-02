package com.example.myprojectgestures2;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitInstance {
    private static Retrofit retrofit;
    static {
        retrofit = new Retrofit.Builder()
                .baseUrl("https://raw.githubusercontent.com/ahren16/Gestures_Base/main/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

    }
    public static GesturesService createService(){
        return retrofit.create(GesturesService.class);
    }
}
