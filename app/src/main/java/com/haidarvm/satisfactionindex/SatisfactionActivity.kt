package com.haidarvm.satisfactionindex

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
    private val baseUrl = "http://indeks.haidarvm.com/"

    var myPreferences = "preferable"
    private var EMPTY = "";
    private val serverSatisfaction = "server"
    private val deptChoosePref = "department"

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_thanks)

        val sharedPreferences: SharedPreferences = getSharedPreferences(myPreferences, 0)
        val domainSharedPref = sharedPreferences.getString(serverSatisfaction, null)
        val deptChoose = sharedPreferences.getString(deptChoosePref, null)
        if (domainSharedPref != null && deptChoose != null) {
//            Log.e("---- yaa Satis nya--", domainSharedPref)
            val completeDomain = "http://" + domainSharedPref.toString()
//            Toast.makeText(applicationContext, "****dari Satis " + completeDomain, Toast.LENGTH_SHORT).show()
//            Log.e("---- yaa Satis nya--", completeDomain)

            val retrofit = Retrofit.Builder()
                .baseUrl(completeDomain)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
            val service = retrofit.create(ScoreService::class.java)

            val scores = intent.extras?.get("scores")

            val gsonJson = JsonObject()
            val deptJson = JsonObject()
            deptJson.addProperty("id", deptChoose)
            gsonJson.add("department", deptJson)
            gsonJson.addProperty("score", scores.toString())


            val call = service.addScore(gsonJson)
            call.enqueue(object : Callback<ScoreModel> {
                override fun onFailure(call: Call<ScoreModel>, t: Throwable) {
                    Log.e("error", t.message.toString())
                    Toast.makeText(baseContext, "Maaf terjadi gangguan silahkan check koneksi server", Toast.LENGTH_LONG).show()
//                    sharedPreferences.edit().clear().commit() // don't use it yet
                }

                override fun onResponse(call: Call<ScoreModel>, response: Response<ScoreModel>) {
                    Log.d("message = ", response.message())
                    if (response.isSuccessful) {
                        val gson = Gson()
                        val responseBody = gson.toJson(response.body())
                        val stringResponse = response.body()?.score
                        Log.d("---- JSON RESPONSE is--", responseBody)
                        Log.d("debug this haidar", stringResponse.toString())
//                        Toast.makeText(baseContext, responseBody, Toast.LENGTH_LONG).show()
                        Log.d("-----isSuccess----", "hai")
                    } else {
//                    sharedPreferences.edit().clear().commit()
                        Log.d("-----isFalse-----", "hai")
                    }
                }

            })

            val timer = Timer()
            timer.schedule(timerTask {
                val intent = Intent(Intent(this@SatisfactionActivity, MainActivity::class.java))
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivityForResult(intent, 0);
                overridePendingTransition(0,0);
            }, 3000)


        } else {
            Log.d("^^^Satis EMPTY^^","jelek")
        }
    }
}

