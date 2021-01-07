package com.example.carrotmarket.myInfo

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.core.content.ContextCompat
import com.example.carrotmarket.R
import kotlinx.android.synthetic.main.activity_set_my_location.*

const val PERMISSION_REQUEST_LOCATION = 0

class SetMyLocationActivity : AppCompatActivity() {
    private val TAG: String = "[SetMyLocationActivity]"

    private lateinit var seekBar: SeekBar
    private var addressTextView = arrayOfNulls<TextView>(2)
    private var addLocationButton = arrayOfNulls<Button>(2)
    private var addLocationLayout = arrayOfNulls<LinearLayout>(2)
    private var deleteLocationButton = arrayOfNulls<Button>(2)
    private var locationTextView = arrayOfNulls<TextView>(2)

    private var position:Int = 0
    
    private var fullAddress = arrayOfNulls<String?>(2)
    val seekBarText: Array<String?> = arrayOfNulls(4)

    @SuppressLint("ResourceAsColor")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_set_my_location)


        addressTextView[0] = findViewById(R.id.locationTextView1)
        addressTextView[1] = findViewById(R.id.locationTextView2)
        addLocationButton[0] = findViewById(R.id.addLocationButton1)
        addLocationButton[1] = findViewById(R.id.addLocationButton2)
        locationTextView[0] = findViewById(R.id.locationTextView1)
        locationTextView[1] = findViewById(R.id.locationTextView2)
        deleteLocationButton[0] = findViewById(R.id.deleteLocationButton1)
        deleteLocationButton[1] = findViewById(R.id.deleteLocationButton2)
        addLocationLayout[0] = findViewById(R.id.addLocationLayout1)
        addLocationLayout[1] = findViewById(R.id.addLocationLayout2)
        seekBar = findViewById(R.id.seekBar)


        // 버튼 설정
        for(i in 0..1){
            // 지역 추가 버튼 누를 때
            addLocationButton[i]?.setOnClickListener {
                Log.d(TAG, "지역 추가 버튼[${i}] 클릭")
                position = i
                addLocateButtonClicked()
            }
            // 지역 삭제 버튼
            deleteLocationButton[i]?.setOnClickListener {
                Log.d(TAG, "지역 삭제 버튼[${i}] 클릭")
                fullAddress[i] = null
                setAddLocationView()
                changePosition()
                for(i in 0..1){
                    Log.d(TAG, "onCreate: ${fullAddress[i]}")
                }
            }
            addressTextView[i]?.setOnClickListener{
                Log.d(TAG, "지역[${i}] 클릭")
                position = i
                changePosition()
            }
        }

        // SeekBar 설정
        seekBar.setOnSeekBarChangeListener(SeekListener())

        // 추가 버튼, 지역 선택 뷰 초기 셋팅
        setAddLocationView()

    }

    private fun changePosition(){
        Log.d(TAG, "changePosition: 실행")
        for(i in 0..1){
            if(i == position && fullAddress[position] != null){
                addLocationLayout[position]?.setBackgroundColor(ContextCompat.getColor(this, R.color.danggeun))
                locationTextView[position]?.setTextColor(ContextCompat.getColor(this, R.color.white))
                addLocationButton[position]?.setTextColor(ContextCompat.getColor(this, R.color.white))
                continue
            }
            addLocationLayout[i]?.setBackgroundColor(ContextCompat.getColor(this, R.color.white))
            locationTextView[i]?.setTextColor(ContextCompat.getColor(this, R.color.grey))
            addLocationButton[i]?.setTextColor(ContextCompat.getColor(this, R.color.grey))
        }

    }

    private fun setJson(location1: String?, location2: String?){
        if(location1 == null && location2 != null){
            fullAddress[0] = location2
            fullAddress[1] = location1
        } else {
            fullAddress[0]  = location1
            fullAddress[2]  = location2
        }
    }

    private fun setAddLocationView(){
        Log.d(TAG, "setAddLocationView: 실행")
        if(fullAddress[0] == null && fullAddress[1] != null){
            fullAddress[0] = fullAddress[1]
            fullAddress[1] = null
            position = 0
        }
        for(i in 0..1) {
            if (fullAddress[i] == null) {
                // 값이 없을 때 addButton
                addLocationLayout[i]?.visibility = View.GONE
                addLocationButton[i]?.visibility = View.VISIBLE
            } else {
                // 값이 있을 때 layout, 데이터 셋팅
                addressTextView[i]?.text = fullAddress[i]?.substring(fullAddress[i]?.lastIndexOf(" ")!! + 1)
                addLocationLayout[i]?.visibility = View.VISIBLE
                addLocationButton[i]?.visibility = View.GONE
            }
        }
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
                fullAddress[position] = data!!.getStringExtra("fullAddress")
                setAddLocationView()
                changePosition()
            }
        }
    }

    inner class SeekListener:SeekBar.OnSeekBarChangeListener{
        // SeekBar의 값이 변경되었을 때
        override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
            selectedLocationTextView.text = seekBarText[progress]
        }
        // SeekBar의 값을 변경하기 위해 터치 하였을 때
        override fun onStartTrackingTouch(seekBar: SeekBar?) {
        }
        // SeekBar의 값을 변경하고 터치를 땠을 때
        override fun onStopTrackingTouch(seekBar: SeekBar?) {
        }
    }

}