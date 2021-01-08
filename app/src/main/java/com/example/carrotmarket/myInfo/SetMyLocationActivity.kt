package com.example.carrotmarket.myInfo

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.location.Address
import android.location.Geocoder
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.carrotmarket.R
import com.example.carrotmarket.myInfo.adpater.AddressListAdapter
import kotlinx.android.synthetic.main.activity_set_my_location.*
import java.util.*
import kotlin.collections.ArrayList
import kotlin.math.round


const val PERMISSION_REQUEST_LOCATION = 0

class SetMyLocationActivity : AppCompatActivity() {
    private val TAG: String = "[SetMyLocationActivity]"

    private lateinit var seekBar: SeekBar
    private var addressTextView = arrayOfNulls<TextView>(2)
    private var addLocationButton = arrayOfNulls<Button>(2)
    private var addLocationLayout = arrayOfNulls<LinearLayout>(2)
    private var deleteLocationButton = arrayOfNulls<Button>(2)
    private var locationTextView = arrayOfNulls<TextView>(2)

    private lateinit var mainLayout: LinearLayout
    private lateinit var subLayout: LinearLayout

    private lateinit var subLayoutCenterTextView: TextView
    private lateinit var subLayoutBackButton: Button

    // position은 0 or 1로 사용자의 지역정보 두개를 표현
    private var position:Int = 0
    
    private var fullAddress = arrayOfNulls<String?>(2)
    var address: Array<TreeSet<String>?> = arrayOfNulls(4)

    @SuppressLint("ResourceAsColor")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_set_my_location)

        mainLayout = findViewById(R.id.main)
        subLayout = findViewById(R.id.sub)

        subLayoutBackButton = findViewById(R.id.subLayoutBackButton)
        subLayoutCenterTextView = findViewById(R.id.subLayoutCenterTextView)

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

        subLayoutBackButton.setOnClickListener{
            main.visibility = View.VISIBLE
            sub.visibility = View.GONE
        }

        // SeekBar 설정
        seekBar.setOnSeekBarChangeListener(SeekListener())

        // 추가 버튼, 지역 선택 뷰 초기 셋팅
        main.visibility = View.VISIBLE
        sub.visibility = View.GONE
        setAddLocationView()

    }

    // 포커스 바꾸기 (지역이 둘다 있을 때 활성화)
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
            deleteLocationButton[i]?.setTextColor(ContextCompat.getColor(this, R.color.grey))
        }
        setRangeLocationFromLocationName()
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

    // 추가 버튼인지 지역인지 셋팅
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

    // 지역 추가
    private fun addLocateButtonClicked() {
        val intent: Intent = Intent(this, SearchLocationActivity::class.java)
        intent.putExtra("callingActivity", 1)
        startActivityForResult(intent, 1)
    }

    private fun setRangeLocationFromLocationName(){
        Log.d(TAG, "setRangeLocationFromLocationName: 실행")
        val length = 0.006
        val geocoder = Geocoder(this, Locale.KOREAN)

        val tempAddress: Address = geocoder.getFromLocationName(fullAddress[position], 1).get(0)
        val latitude: Double = round(tempAddress.latitude * 1000) /1000
        val longitude: Double = round(tempAddress.longitude * 1000) /1000
        var list: List<Address>
        for(a in 0..3){
            address[a] = TreeSet()
            for(i in 0-a..0+a){
                for(k in 0-a..0+a){
                    list = geocoder.getFromLocation(latitude + i * length, longitude + k * length, 1)
                    if(list.isNotEmpty()
                            && list[0].adminArea != null
                            && list[0].locality != null
                            && list[0].subLocality != null
                            && list[0].thoroughfare != null)
                            {
                                address[a]?.add(list[0].thoroughfare)
                    }
                }
            }
        }
        setSeekBarPosition(0)
    }

    @SuppressLint("SetTextI18n")
    private fun setSeekBarPosition(progress: Int){
        selectedLocationTextView.text =
                "${fullAddress[position]?.substring(fullAddress[position]?.lastIndexOf(" ")!! + 1)} 근처 동네 ${address[progress]?.size}개"

        val spannableString = SpannableString(selectedLocationTextView.text)
        val clickableSpan: ClickableSpan = object : ClickableSpan() {
            override fun onClick(textView: View) {
                Log.e(TAG, "onClick: " + address[progress])
                val list = mutableListOf<String>()

                for (i in address[progress]!!) list.add(i)
                Log.e(TAG, "onClick: list = $list")
                val addressAdapter = AddressListAdapter(this@SetMyLocationActivity,
                    list as ArrayList<String>
                )
                val addressListView = findViewById<ListView>(R.id.addressListView)
                addressListView.adapter = addressAdapter

                mainLayout.visibility = View.GONE
                subLayout.visibility = View.VISIBLE
            }
        }
        val start = selectedLocationTextView.text.indexOf("근처 동네")
        val end = selectedLocationTextView.text.length

        // 근처 동네 부터 이벤트, 스타일 지정
        spannableString.setSpan(clickableSpan,start,end,Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        spannableString.setSpan(StyleSpan(Typeface.BOLD), start, end, Spanned.SPAN_EXCLUSIVE_INCLUSIVE)
        spannableString.setSpan(ForegroundColorSpan(Color.BLACK), start, end, Spanned.SPAN_EXCLUSIVE_INCLUSIVE);

        selectedLocationTextView.text = spannableString
        selectedLocationTextView.movementMethod = LinkMovementMethod.getInstance()

        seekBar.progress = progress
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == 1){
            if(resultCode == RESULT_OK) {
                fullAddress[position] = data!!.getStringExtra("fullAddress")
                setRangeLocationFromLocationName()
                setAddLocationView()
                changePosition()
            }
        }
    }

    inner class SeekListener:SeekBar.OnSeekBarChangeListener{
        // SeekBar의 값이 변경되었을 때
        override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
            setSeekBarPosition(progress)
        }
        // SeekBar의 값을 변경하기 위해 터치 하였을 때
        override fun onStartTrackingTouch(seekBar: SeekBar?) {
        }
        // SeekBar의 값을 변경하고 터치를 땠을 때
        override fun onStopTrackingTouch(seekBar: SeekBar?) {
        }
    }

}