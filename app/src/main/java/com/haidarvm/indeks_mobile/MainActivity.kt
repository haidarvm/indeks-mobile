package com.haidarvm.indeks_mobile

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    // TODO : if no sharedpreferance redirect to input domain & choose service

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btnSatisfaction.setOnClickListener {
            val intent = Intent(this, SatisfactionActivity::class.java)
            intent.putExtra("scores", 1)
            startActivity(intent)
        }

        btnDissatisfaction.setOnClickListener {
            val intent = Intent(this, SatisfactionActivity::class.java)
            intent.putExtra("scores", -1)
            startActivity(intent)
        }

    }






}
