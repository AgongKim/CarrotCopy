package com.example.carrotmarket

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.LinearLayout

class SetMyLocateActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_set_my_locate)
        val addLocateButton1 = findViewById<Button>(R.id.addLocateButton1)
        val addLocateButton2 = findViewById<Button>(R.id.addLocateButton2)
        val backButton = findViewById<Button>(R.id.backButton)
        val setLocate = findViewById<Button>(R.id.setLocateButton)

        val locateLayout = findViewById<LinearLayout>(R.id.locate)
        val locateDetailLayout = findViewById<LinearLayout>(R.id.lcoate_detail)

        // 초기 셋팅
        locateLayout.visibility = View.VISIBLE
        locateDetailLayout.visibility = View.GONE

        addLocateButton1.setOnClickListener {
            locateLayout.visibility = View.GONE
            locateDetailLayout.visibility = View.VISIBLE
        }

        addLocateButton2.setOnClickListener {
            locateLayout.visibility = View.GONE
            locateDetailLayout.visibility = View.VISIBLE
        }

        backButton.setOnClickListener {
            locateLayout.visibility = View.VISIBLE
            locateDetailLayout.visibility = View.GONE
        }

        setLocate.setOnClickListener {

        }



    }
}