package com.example.carrotmarket

import android.Manifest
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.location.*
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Toast
import androidx.annotation.NonNull
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.text.set
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import java.io.IOException
import java.util.*
import kotlin.collections.HashSet
import kotlin.math.round

const val PERMISSION_REQUEST_LOCATION = 0

class SetMyLocateActivity : AppCompatActivity() {
    private val TAG: String = "[SetMyLocateActivity]"
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var geocoder: Geocoder

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
        findViewById<EditText>(R.id.rangeEditText).setText("1")

        // 버튼 설정
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

        // 위치 정보 가져오기
        setLocate.setOnClickListener {
            // 체크 퍼미션 실행
            // 권한 있으면 getLocation() 실행
            // 권한 없으면 권한 요청
            checkPermission()
        }

    }

    private fun getLocation(){
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Log.d(TAG, "getLocation: 권한 없음 종료")
            return
        }
        Log.d(TAG, "getLocation: 실행")
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        geocoder = Geocoder(this, Locale.KOREAN)
        fusedLocationClient.lastLocation
                .addOnSuccessListener { location : Location? ->
                    if (location != null) {
                        // 소수점 세자리 이하는 오차 범위 100m 이내로 동이름 가져오는데에 무리 없으므로 자름
                        val range = findViewById<EditText>(R.id.rangeEditText).text.toString().toIntOrNull()

                        when (range) {
                            1 -> {
                                getLocationInRange(0, location)
                            }
                            2 -> {
                                getLocationInRange(1, location)
                            }
                            3 -> {
                                getLocationInRange(2, location)
                            }
                            4 -> {
                                getLocationInRange(3, location)
                            }
                        }
                    }
                }
    }

    private fun getLocationInRange(range: Int, location: Location){
        val length = 0.006
        val latitude: Double = round(location.latitude*1000)/1000
        val longitude: Double = round(location.longitude*1000)/1000
        var address: TreeSet<String> = sortedSetOf()
        var list: List<Address>
        for(i in -range..range){
            for(k in -range..range){
                list = geocoder.getFromLocation(latitude+i*length, longitude+k*length, 1)
                if(list.isNotEmpty()){
                    address.add(list[0].thoroughfare)
                }
            }
        }
        Log.e(TAG, "getLocation: ${address}")
        address.clear()
    }


    private fun checkPermission() {
        Log.d(TAG, "checkPermission: 실행")
        if(ContextCompat.checkSelfPermission(this@SetMyLocateActivity, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(this@SetMyLocateActivity, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            Log.d(TAG, "checkPermission: 권한 있음")
            // 실행 코드
            getLocation()
        } else {
            // 권한 없으면 권한 요청
            Log.d(TAG, "checkPermission: 권한 없음")
            requestLoationPermission()
        }
    }

    private fun requestLoationPermission(){
        if(ActivityCompat.shouldShowRequestPermissionRationale(this@SetMyLocateActivity, Manifest.permission.ACCESS_COARSE_LOCATION) &&
                ActivityCompat.shouldShowRequestPermissionRationale(this@SetMyLocateActivity, Manifest.permission.ACCESS_FINE_LOCATION)){
            // 권한 요구
            Log.d(TAG, "requestLoationPermission: 권한 요청")
            ActivityCompat.requestPermissions(
                    this@SetMyLocateActivity,
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION),
                    PERMISSION_REQUEST_LOCATION)
        } else {
            // 다시 묻지 않음을 누르고 거부하고 요청했을떄
            Log.d(TAG, "requestLoationPermission: 다시 묻지 않음을 누르고 거부 후, 권한 요청")
            AlertDialog.Builder(this)
                    .setTitle("알림")
                    .setMessage("GPS 권한이 거부되었다네. 사용을 원한다면 설정에서 해당 권한을 직접 허용하게.")
                    .setNeutralButton("설정", object: DialogInterface.OnClickListener {
                        override fun onClick(dialogInterface:DialogInterface, i:Int) {
                            val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                            intent.setData(Uri.parse("package:" + getPackageName()))
                            startActivity(intent)
                        }
                    })
                    .setPositiveButton("확인", object:DialogInterface.OnClickListener {
                        override fun onClick(dialogInterface:DialogInterface, i:Int) {
                            dialogInterface.cancel()
                        }
                    })
                    .setCancelable(false)
                    .create()
                    .show()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(requestCode == PERMISSION_REQUEST_LOCATION){
            if(grantResults.size == 2
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED
                    && grantResults[1] == PackageManager.PERMISSION_GRANTED){
                // 권찬 요쳥 수락
                Log.d(TAG, "onRequestPermissionsResult: 권한 요청 수락")
                // 코드 실행
            } else {
                Log.d(TAG, "onRequestPermissionsResult: 권한 요청 거부")
                // 권한 요청 거부
            }
        }
    }


}