package com.example.carrotmarket

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.LinearLayout

class SetMyLocateActivity : AppCompatActivity() {
    private lateinit var addLocateButton1: Button
    private lateinit var addLocateButton2: Button
    private lateinit var backButton: Button
    private lateinit var locateLayout: LinearLayout
    private lateinit var locateDetailLayout: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_set_my_locate)
        addLocateButton1 = findViewById(R.id.addLocateButton1)
        addLocateButton2 = findViewById(R.id.addLocateButton2)
        backButton = findViewById(R.id.backButton)

        locateLayout = findViewById(R.id.locate)
        locateDetailLayout = findViewById(R.id.lcoate_detail)

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




    }
}