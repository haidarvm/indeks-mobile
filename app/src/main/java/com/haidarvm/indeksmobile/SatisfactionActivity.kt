package com.haidarvm.indeksmobile

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*
import kotlin.concurrent.timerTask


class SatisfactionActivity : AppCompatActivity() {
    private val baseUrl = "http://info.haidar.id/"

    var myPreferences = "preferable"
    private var EMPTY = "";
    private val serverSatisfaction = "server"

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        val sharedPreferences: SharedPreferences = getSharedPreferences(myPreferences, 0)
        val domainSharedPref = sharedPreferences.getString(serverSatisfaction, null)
        if (domainSharedPref == null) {
            Log.e("^^^Satis EMPTY^^","jelek")
        } else {
            Log.e("---- yaa Satis nya--", domainSharedPref)
            val completeDomain = "http://" + domainSharedPref.toString()
            Toast.makeText(applicationContext, "****dari Satis " + completeDomain, Toast.LENGTH_SHORT).show()
            Log.e("---- yaa Satis nya--", completeDomain)

            val retrofit = Retrofit.Builder()
                .baseUrl(completeDomain)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
            val service = retrofit.create(ScoreService::class.java)

            val scores = intent.extras?.get("scores")

            val gsonJson = JsonObject()
            val deptJson = JsonObject()
            deptJson.addProperty("id", "2")
            gsonJson.add("department", deptJson)
            gsonJson.addProperty("score", scores.toString())


            val call = service.addScore(gsonJson)
            Log.e("mana sih", gsonJson.toString())
            call.enqueue(object : Callback<ScoreModel> {
                override fun onFailure(call: Call<ScoreModel>, t: Throwable) {
                    Log.e("error", t.message.toString())
                    sharedPreferences.edit().clear().commit()
                }

                override fun onResponse(call: Call<ScoreModel>, response: Response<ScoreModel>) {
                    Log.d("message = ", response.message())
                    if (response.isSuccessful) {
                        val gson = Gson()
                        val responseBody = gson.toJson(response.body())
                        val stringResponse = response.body()?.score
                        Log.e("---- JSON RESPONSE is--", responseBody)
                        Log.e("debug this haidar", stringResponse.toString())
                        Toast.makeText(baseContext, responseBody, Toast.LENGTH_LONG).show()
                        Log.e("-----isSuccess----", "hai")
                    } else {
//                    sharedPreferences.edit().clear().commit()
                        Log.e("-----isFalse-----", "hai")
                    }
                }

            })

            val timer = Timer()
            timer.schedule(timerTask {
                startActivity(Intent(this@SatisfactionActivity, MainActivity::class.java))
            }, 3000)
        }
    }
}

