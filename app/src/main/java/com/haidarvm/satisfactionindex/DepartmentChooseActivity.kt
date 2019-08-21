package com.haidarvm.satisfactionindex

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import com.google.gson.reflect.TypeToken
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import android.widget.ArrayAdapter
import com.google.gson.JsonObject
import kotlinx.android.synthetic.main.activity_department_choose.*


class DepartmentChooseActivity : AppCompatActivity() {

    private var EMPTY = ""
    var myPreferences = "preferable"
    var httpAdd = "http://"
    private val serverSatisfaction = "server"
    private val deptChoosePref = "department"
    private val deptChooseNamePref = "department_name"
    private val deptChooseTextPref = "department_text"
    private val deptChooseBgPref = "department_bg_color"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_department_choose)
//        spinner = (Spinner)findViewById(R.id.department_name);

        val spinnerFrom: Spinner = findViewById(R.id.department_name)
//        val deviceId = Settings.Secure.getString(
//            contentResolver,
//            Settings.Secure.ANDROID_ID);


        val sharedPreferences: SharedPreferences = this.getSharedPreferences(myPreferences, Context.MODE_PRIVATE)
        val domainSharedPref = sharedPreferences.getString(serverSatisfaction, EMPTY)
        val retrofit = Retrofit.Builder()
            .baseUrl(httpAdd + domainSharedPref)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val deptService = retrofit.create(DepartmentService::class.java)
//        Log.e("----dptdomain---", domainSharedPref)
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
//                    Log.e("---- JSON RESPONSE is--", response.body().toString())
//                                var stringJson = response.body();
                    val gson = Gson()
                    val jsonDept = gson.toJson(response.body())
                    Log.e("---- RealJSON is--", jsonDept)

                    val typeToken = object : TypeToken<ArrayList<DepartmentModel>>() {}
                    Log.e("***dept-list**", jsonDept.toString())
                    val listArray = gson.fromJson<ArrayList<DepartmentModel>>(jsonDept, typeToken.type)
                    val spinnerArr = ArrayList<String>()
                    for (deptName in listArray) {
//                        Log.e("**spin name*", deptName.name)
                        spinnerArr.add(deptName.id.toString())
                    }


                    val deptArrList = ArrayList<String>()
                    for (deptDrop in listArray) {
//                        Log.e("**Dropdown", deptDrop.name + deptDrop.id)
                        deptArrList.add(deptDrop.name + " (" + deptDrop.id + ")")
                    }

                    spinnerFrom.adapter = ArrayAdapter<String>(
                        this@DepartmentChooseActivity,
                        android.R.layout.simple_spinner_dropdown_item,
                        deptArrList
                    )

                    spinnerFrom.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                        override fun onNothingSelected(p0: AdapterView<*>?) {
//                            Log.e("do nothing", "null")
                        }

                        override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                            val selectedDept = parent.selectedItem.toString();
                            val selectedDeptId =
                                selectedDept.substring(selectedDept.indexOf("(") + 1, selectedDept.indexOf(")"))
//                            Log.e(" ^^^Sel ID**" , selectedDeptId)
                            button_dept_choose.setOnClickListener {
                                //                                Log.e("buttonChoose", selectedDeptId)
                                val retrofit = Retrofit.Builder()
                                    .baseUrl(httpAdd + domainSharedPref)
                                    .addConverterFactory(GsonConverterFactory.create())
                                    .build()
                                val deptService = retrofit.create(DepartmentService::class.java)
                                Log.d("----dptdomain---", domainSharedPref)

                                val deptSelJson = JsonObject()
                                deptSelJson.addProperty("id", selectedDeptId)
                                deptSelJson.addProperty("available", 0)
                                deptSelJson.addProperty("deviceId", "opppo")
//                                Log.e("---Device is--", "oppo")
//                                Log.e("---JsonSent -", deptSelJson.toString())
                                val call = deptService.setDepartment(deptSelJson)
                                call.enqueue(object : Callback<DepartmentModel> {
                                    override fun onFailure(call: Call<DepartmentModel>, t: Throwable) {
                                        Log.e("error", t.message.toString())
                                        sharedPreferences.edit().clear().commit()
                                    }

                                    override fun onResponse(
                                        call: Call<DepartmentModel>,
                                        response: Response<DepartmentModel>
                                    ) {
//                                        Log.d("----RESPONSE is--", gson.toJson(response.toString()).toString())
                                        if (response.isSuccessful) {
                                            val responseBody = gson.toJson(response.body())
//                                            Toast.makeText(baseContext, responseBody, Toast.LENGTH_LONG).show()
//                                            Log.e("---- JSON RES DEPTis--", responseBody.toString())
//                                            Log.e("-----isSuccess----", "hai")
                                            val editor = sharedPreferences.edit()
                                            editor.putString(deptChoosePref, selectedDeptId)
                                            val gson = Gson()
                                            val deptAll = Gson().fromJson(responseBody, DepartmentModel::class.java)
                                            val textService = deptAll.textService
                                            editor.putString(deptChooseTextPref, textService)
                                            editor.putString(deptChooseNamePref, deptAll.name)
                                            editor.putString(deptChooseBgPref, "#" + deptAll.bgColor)
                                            Log.e("deptChooseText=", textService)

                                            editor.apply()

                                            val deptChoose = sharedPreferences.getString(deptChoosePref, null)
//                                            Log.e("deptChoosePref =", deptChoose)
                                            redirectMain()
                                        } else {
                                            Log.e("-----isFalse-----", "hai")
                                        }
                                    }
                                })
                            }
                        }

                    }


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
        Log.d("---- yaa Input nya--", domainSharedPref)
//        } else {
//            Log.e("---- Gagal nya--", domainSharedPref)
//        }
    }

    private fun redirectMain() {
        val sd = Intent(this, MainActivity::class.java)
        startActivity(sd)
    }


}