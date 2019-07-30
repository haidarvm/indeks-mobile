package com.haidarvm.indeksmobile

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_department_choose.*
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import org.json.JSONArray
import com.google.gson.reflect.TypeToken
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import android.widget.ArrayAdapter




class DepartmentChooseActivity : AppCompatActivity() {

    private var EMPTY = "";
    var myPreferences = "preferable"
    var httpAdd = "http://";
    private val serverSatisfaction = "server"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_department_choose)
//        spinner = (Spinner)findViewById(R.id.department_name);

        val spinnerFrom: Spinner = findViewById(R.id.department_name) as Spinner


        val sharedPreferences: SharedPreferences = this.getSharedPreferences(myPreferences, Context.MODE_PRIVATE)
        val domainSharedPref = sharedPreferences.getString(serverSatisfaction, EMPTY)
        val retrofit = Retrofit.Builder()
            .baseUrl(httpAdd + domainSharedPref)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val deptService = retrofit.create(DepartmentService::class.java)
        Log.e("----dptdomain---", domainSharedPref)
        val call = deptService.getDepartment()
        call.enqueue(object : Callback<List<DepartmentModel>> {
            override fun onFailure(call: Call<List<DepartmentModel>>, t: Throwable) {
                Log.e("error list Deptnya", t.message.toString())
            }

            override fun onResponse(
                call: Call<List<DepartmentModel>>,
                response: Response<List<DepartmentModel>>
            ) {

                if (response.isSuccessful) {
                    Log.e("---- JSON RESPONSE is--", response.body().toString())
//                                var stringJson = response.body();
                    val gson = Gson()
                    val jsonDept = gson.toJson(response.body())
                    Log.e("---- RealJSON is--", jsonDept)
                    val jsonArray = JSONArray(jsonDept)
                    (0..5).forEach { index ->
                        val jsonObject = jsonArray.getJSONObject(index)
                        if (jsonObject.has("name") && jsonObject.has("available")) {
                            var deptName = jsonObject.getString("name")
                            var deptAvailble = jsonObject.getString("available")
                            Log.e("***dept-name**", deptName)
                            Log.e("***dept-ava**", deptAvailble)
                        }
                    }

                    val typeToken = object : TypeToken<ArrayList<DepartmentModel>>() {}
                    Log.e("***dept-list**", jsonDept.toString())
                    val listArray = gson.fromJson<ArrayList<DepartmentModel>>(jsonDept, typeToken.type)
                    val spinnerArr = ArrayList<String>()
                    for (deptName in listArray) {
                        Log.e("**spin name*", deptName.name)
                        spinnerArr.add(deptName.name)
                    }
                    spinnerFrom.setAdapter(
                        ArrayAdapter<String>(
                            this@DepartmentChooseActivity,
                            android.R.layout.simple_spinner_dropdown_item,
                            spinnerArr
                        )
                    )


                } else {
                    Log.e("-----isFalseMain-", response.toString())
                }
            }

        }) // end call


//            Toast.makeText(applicationContext, "****dari input " + departmentList.toString(), Toast.LENGTH_SHORT).show()
//        val deptJson = JsonObject()
//        deptJson.addProperty("available", 0)
//        deptJson.addProperty("id", 1)

//            val call = deptService.setDepartment(1)
//            call.enqueue(object : Callback<DepartmentModel> {
//                override fun onFailure(call: Call<DepartmentModel>, t: Throwable) {
//                    Log.e("error", t.message.toString())
//                }
//
//                override fun onResponse(call: Call<DepartmentModel>, response: Response<DepartmentModel>) {
//                    Log.e("**Contain** = ", response.message())
//                    if (response.isSuccessful) {
//                        val gson = Gson()
//                        val responseBody = gson.toJson(response.body())
//                        Log.e("---- JSON RESPONSE is--", responseBody)
//                        Toast.makeText(baseContext, responseBody, Toast.LENGTH_LONG).show()
//                    } else {
//                        Log.e("-----isFalse-----", "damn it")
//                    }
//                }
//
//
//            })

        Toast.makeText(applicationContext, "****dari input " + domainSharedPref, Toast.LENGTH_SHORT).show()
        Log.e("---- yaa Input nya--", domainSharedPref)
//        } else {
//            Log.e("---- Gagal nya--", domainSharedPref)
//        }
    }


}