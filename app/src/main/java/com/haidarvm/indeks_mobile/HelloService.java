package com.haidarvm.indeks_mobile;

import retrofit2.Call;
import retrofit2.http.GET;


public interface HelloService {

    @GET("api/score")
    Call<HelloModel> message();
}