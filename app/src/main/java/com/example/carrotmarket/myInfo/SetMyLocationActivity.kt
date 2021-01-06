package com.example.carrotmarket.myInfo

import android.content.Intent
import android.location.*
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*
import com.example.carrotmarket.R
import kotlinx.android.synthetic.main.activity_set_my_location.*
import java.util.*

const val PERMISSION_REQUEST_LOCATION = 0

class SetMyLocationActivity : AppCompatActivity() {
    private val TAG: String = "[SetMyLocationActivity]"

    private lateinit var addressTextView1: TextView
    private lateinit var addressTextView2: TextView
    private lateinit var seekBar: SeekBar

    private var locationPosition:Int = 0

    val tempListSize: Array<Int?> = arrayOfNulls(4)
    val seekBarText: Array<String?> = arrayOfNulls(4)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_set_my_location)
        val addLocationButton1 = findViewById<Button>(R.id.addLocationButton1)
        val addLocationButton2 = findViewById<Button>(R.id.addLocationButton2)

        addressTextView1 = findViewById(R.id.addressTextView1)
        addressTextView2 = findViewById(R.id.addressTextView2)
        seekBar = findViewById(R.id.seekBar)

        // 버튼 설정
        // 지역 추가 버튼 누를 때
        addLocationButton1.setOnClickListener {
            addLocateButtonClicked()
            locationPosition = 1
        }
        addLocationButton2.setOnClickListener {
            addLocateButtonClicked()
            locationPosition = 2
        }

        // SeekBar 설정
        seekBar.setOnSeekBarChangeListener(SeekListener())

        // 임시 데이터
        setJson()

    }

    private fun setJson(){
        // 지역 하나는 저장 되어있어야함
        addressTextView1.text = "삼평동"
    }

    private fun addLocateButtonClicked() {
        val intent: Intent = Intent(this, SearchLocationActivity::class.java)
        intent.putExtra("callingActivity", 1)
        startActivityForResult(intent, 1)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == 1){
            if(resultCode == RESULT_OK) {
                addressTextView1.text = data!!.getStringExtra("location")
            }
        }
    }

    inner class SeekListener:SeekBar.OnSeekBarChangeListener{
        // SeekBar의 값이 변경되었을 때
        override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
            addressCenterTextView.text = seekBarText[progress]
        }
        // SeekBar의 값을 변경하기 위해 터치 하였을 때
        override fun onStartTrackingTouch(seekBar: SeekBar?) {
        }
        // SeekBar의 값을 변경하고 터치를 땠을 때
        override fun onStopTrackingTouch(seekBar: SeekBar?) {
        }
    }

}