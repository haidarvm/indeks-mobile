package com.haidarvm.indeks_mobile

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*
import android.util.Log.d as d1


class MainActivity : AppCompatActivity() {



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btnSatisfaction.setOnClickListener {
            // Handler code here.
            val intent = Intent(this, SatisfactionActivity::class.java)
            startActivity(intent)
        }

    }






}
