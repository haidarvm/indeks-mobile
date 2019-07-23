package com.haidarvm.indeks_mobile

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import android.content.Intent
import java.util.*
import kotlin.concurrent.timerTask


class SatisfactionActivity : AppCompatActivity() {
    private val baseUrl = "http://192.168.1.7:8080/"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val retrofit = Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val service = retrofit.create(HelloService::class.java)
        val call = service.message()
        call.enqueue(object : Callback<HelloModel> {
            override fun onFailure(call: Call<HelloModel>, t: Throwable) {
                Log.e("error", t.message.toString())
            }

            override fun onResponse(call: Call<HelloModel>, response: Response<HelloModel>) {
//                val stringResponse = response.body().toString()
                val stringResponse = response.body()?.message
                Log.d("debug this haidar", stringResponse.toString())
                Toast.makeText(baseContext, stringResponse, Toast.LENGTH_LONG).show()

            }


        })
        // if you are redirecting from a fragment then use getActivity() as the context.
        val timer = Timer()
        timer.schedule(timerTask {
            startActivity(Intent(this@SatisfactionActivity, MainActivity::class.java))
        }, 3000)
    }
}