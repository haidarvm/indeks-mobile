package com.haidarvm.indeksmobile

import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST


interface DepartmentService {
    @GET("api/department")
    fun getDepartment(): Call<List<DepartmentModel>>

    @Headers("Content-type: application/json")
    @POST("api/department/")
    fun setDepartment(@Body jsonObject: JsonObject): Call<DepartmentModel>
}