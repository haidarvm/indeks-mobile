package com.haidarvm.indeksmobile

import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.http.*


interface DepartmentService {
    @GET("api/department")
    fun getDepartment(): Call<List<DepartmentModel>>

    @Headers("Content-type: application/json")
    @PUT("api/department")
    fun setDepartment(@Body jsonObject: JsonObject): Call<DepartmentModel>
}