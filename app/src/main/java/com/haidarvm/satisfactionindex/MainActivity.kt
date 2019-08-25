package com.haidarvm.satisfactionindex

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_empty.*
import kotlinx.android.synthetic.main.activity_thanks.*
import kotlinx.android.synthetic.main.content_main.*
import com.bumptech.glide.load.resource.bitmap.TransformationUtils.centerCrop
import com.bumptech.glide.request.RequestOptions




class MainActivity : AppCompatActivity() {

    private var EMPTY = "";
    var myPreferences = "preferable"
    private val serverSatisfaction = "server"
    private val deptChoosePref = "department"
    private val deptChooseNamePref = "department_name"
    private val deptChooseTextPref = "department_text"
    private val deptChooseBgPref = "department_bg_color"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val sharedPreferences: SharedPreferences = getSharedPreferences(myPreferences, 0)
        val domainPerf = sharedPreferences.getString(serverSatisfaction, null)
        val deptChoose = sharedPreferences.getString(deptChoosePref, null)

        if (deptChoose != null && domainPerf != null) {
            setContentView(R.layout.activity_main)
            Log.e("++ Ada INI ++", domainPerf + deptChoose + " warna " + deptChooseBgPref)

            val deptChooseTextPrefVal = sharedPreferences.getString(deptChooseTextPref, null)
            val deptChooseBgPrefVal = sharedPreferences.getString(deptChooseBgPref, null)
            var checkBgPref = deptChooseBgPrefVal ?: "#81B7FF"
            Log.e("warna nya ", checkBgPref)
//            checkBgPref = "".equals(.deptChooseBgPrefVal)
            main_layout.setBackgroundColor(Color.parseColor(checkBgPref))
//            val deptChooseNamePrefVal = sharedPreferences.getString(deptChooseNamePref, null)
//            departmenText.text = deptChooseNamePrefVal
            textService.text = deptChooseTextPrefVal
            Log.e(" deptTextSer =", deptChooseTextPrefVal)
//            http://indeks.haidarvm.com/dept/dissatisfy/3.png
            val options = RequestOptions()
            options.centerCrop().fitCenter()
            GlideApp.with(imgSatisfy.context).load("http://$domainPerf/dept/satisfy/$deptChoose.png").override(185,185).apply(options).into(imgSatisfy)
            GlideApp.with(imgDissatisfy.context).load("http://$domainPerf/dept/dissatisfy/$deptChoose.png").override(185,185).apply(options).into(imgDissatisfy)
            imgSatisfy.setOnClickListener {
                val intent = Intent(this, SatisfactionActivity::class.java)
                intent.putExtra("scores", 1)
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivityForResult(intent, 0);
                overridePendingTransition(0,0);
            }

            imgDissatisfy.setOnClickListener {
                val intent = Intent(this, SatisfactionActivity::class.java)
                intent.putExtra("scores", -1)
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivityForResult(intent, 0);
                overridePendingTransition(0,0);
            }
//            btnSatisfaction.setOnClickListener {
//                val intent = Intent(this, SatisfactionActivity::class.java)
//                intent.putExtra("scores", 1)
//                startActivity(intent)
//            }
//
//            btnDisatisfaction.setOnClickListener {
//                val intent = Intent(this, SatisfactionActivity::class.java)
//                intent.putExtra("scores", -1)
//                startActivity(intent)
//            }
        } else {
            sharedPreferences.edit().clear().commit()
            setContentView(R.layout.activity_empty)
            Log.d("KOSOONGG INI", "jelek")
            Toast.makeText(baseContext, "Hai isi domain dlu yuu", Toast.LENGTH_LONG).show()
            button_set_domain.setOnClickListener {
                val inputtedText = domain_text.text
                if (inputtedText.isNotEmpty()) { // EditText validation
                    savePreferences(serverSatisfaction, inputtedText.toString());

//                    val completeDomainPref = "http://" + domainPerf
//                    savePreferences(serverSatisfaction, completeDomainPref)

                    redirectDeptChoose()

                } else {
                    Toast.makeText(applicationContext, "EditText is empty", Toast.LENGTH_SHORT).show()
                }
            }

        }
//        if (sharedPreferences.getString(serverSatisfaction, EMPTY) != null) {
//            Toast.makeText(applicationContext,sharedPreferences.getString(serverSatisfaction, EMPTY), Toast.LENGTH_SHORT).show()
//
//            btnSatisfaction.setOnClickListener {
//                val intent = Intent(this, SatisfactionActivity::class.java)
//                intent.putExtra("scores", 1)
//                startActivity(intent)
//            }
//
//            btnDissatisfaction.setOnClickListener {
//                val intent = Intent(this, SatisfactionActivity::class.java)
//                intent.putExtra("scores", -1)
//                startActivity(intent)
//            }
//
//        } else {
//            Toast.makeText(baseContext, "Hai isi domain dlu yuu", Toast.LENGTH_LONG).show()
//            button_get.setOnClickListener {
//                val inputtedText = edit_text.text
//                if (inputtedText.isNotEmpty()) { // EditText validation
//                    savePreferences(serverSatisfaction, inputtedText.toString());
//                } else {
//                    Toast.makeText(applicationContext, "EditText is empty", Toast.LENGTH_SHORT).show()
//                }
//            }
//        }
    }

    private fun redirectDeptChoose() {
        val sd = Intent(this, DepartmentChooseActivity::class.java)
        startActivity(sd)
    }

    private fun savePreferences(key: String, value: String) {

        val sharedPreferences: SharedPreferences = this.getSharedPreferences(myPreferences, Context.MODE_PRIVATE)

        val editor = sharedPreferences.edit()
        editor.clear();
        editor.putString(key, value)
        editor.commit()
        val name = sharedPreferences.getString(serverSatisfaction, EMPTY)
        Log.d("---- yaa  New nya--", name)
//        Toast.makeText(applicationContext, name, Toast.LENGTH_SHORT).show()

    }

    override fun onBackPressed() {
        // do something
        Toast.makeText(baseContext, "Maaf tidak tersedia", Toast.LENGTH_LONG).show()
    }
}


