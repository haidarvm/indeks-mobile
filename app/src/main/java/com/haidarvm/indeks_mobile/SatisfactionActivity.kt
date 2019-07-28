package com.haidarvm.indeks_mobile

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import retrofit2.converter.gson.GsonConverterFactory
import android.content.Intent
import android.widget.Toast
import com.google.gson.JsonObject
import retrofit2.*
import java.util.*
import kotlin.concurrent.timerTask
import com.google.gson.Gson


class SatisfactionActivity : AppCompatActivity() {
    private val baseUrl = "http://192.168.1.7:8080/"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val retrofit = Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val service = retrofit.create(ScoreService::class.java)

        val scores = intent.extras?.get("scores")

        val gsonJson = JsonObject()
        val deptJson = JsonObject()
        deptJson.addProperty("id", "1")
        gsonJson.add("department", deptJson)
        gsonJson.addProperty("score", scores.toString())


        val call = service.addScore(gsonJson)
        Log.d("mana sih", gsonJson.toString())
        call.enqueue(object : Callback<ScoreModel> {
            override fun onFailure(call: Call<ScoreModel>, t: Throwable) {
                Log.e("error", t.message.toString())
            }

            override fun onResponse(call: Call<ScoreModel>, response: Response<ScoreModel>) {
                Log.d("message = ", response.message())
                if (response.isSuccessful) {
                    val gson = Gson()
                    val responseBody = gson.toJson(response.body())
                    val stringResponse = response.body()?.score
                    Log.d("---- JSON RESPONSE is--", responseBody)
                    Log.d("debug this haidar", stringResponse.toString())
                    Toast.makeText(baseContext, responseBody, Toast.LENGTH_LONG).show()
                    Log.d("-----isSuccess----", "hai")
                } else {
                    Log.d("-----isFalse-----", "hai")
                }
            }

        })

        val timer = Timer()
        timer.schedule(timerTask {
            startActivity(Intent(this@SatisfactionActivity, MainActivity::class.java))
        }, 3000)
    }
}

