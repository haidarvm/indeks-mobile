package com.haidarvm.indeksmobile

import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface ScoreService {

    @Headers("Content-type: application/json")
    @POST("api/score")
    fun addScore(@Body jsonObject: JsonObject): Call<ScoreModel>
}