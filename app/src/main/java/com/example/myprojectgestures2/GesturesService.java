package com.example.myprojectgestures2;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface GesturesService {
    @GET ("categories.json")
    Call<List<CategoryInfo>> getAllCategories();
    @GET ("videos/{code}.json" )
    Call<VideoInfo> getVideoInfo(@Path("code") String code);
}
