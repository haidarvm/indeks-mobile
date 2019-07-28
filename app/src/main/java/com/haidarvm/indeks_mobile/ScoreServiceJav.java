package com.haidarvm.indeks_mobile;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;



public interface ScoreServiceJav {

    @POST("api/score")
    Call<ScoreModel> getUser(@Body String body);
}
