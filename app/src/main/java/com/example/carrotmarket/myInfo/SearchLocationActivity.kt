package com.example.carrotmarket.myInfo

import android.Manifest
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.widget.Button
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.example.carrotmarket.R
import com.example.carrotmarket.myInfo.adpater.AddressListAdapter
import com.google.android.gms.location.LocationServices
import java.util.*
import kotlin.math.round

class SearchLocationActivity : AppCompatActivity() {
    private val TAG: String = "[SearchLocationActivity]"


    private lateinit var addressListView: ListView

    private var callingActivity: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_location)

        addressListView = findViewById(R.id.addressListView)

        val backButton = findViewById<Button>(R.id.backButton)
        val findLocateButton = findViewById<Button>(R.id.findLocateButton)
        
        // 어떤 액티비티가 불렀는지 기록
        callingActivity = intent.getIntExtra("callingActivity", 0)

        // 뒤로가기
        backButton.setOnClickListener {
            finish()
        }

        // 위치 정보 가져오기
        findLocateButton.setOnClickListener {
            getLocation()
        }

        // 첫 실행시 자동 시작
        getLocation()
    }

    // 리스트 버튼
    private fun setAdapter(addressList: ArrayList<String>) {
        val addressAdapter = AddressListAdapter(this, addressList)
        addressListView.adapter = addressAdapter
        addressListView.setOnItemClickListener { parent, view, position, id ->
            // 리스트에서 주소 클릭했을때 액티비티로 돌아가면서 주소 세팅
            if(callingActivity == 1){
                // 프로필에서 주소 설정
                var intent = Intent(this, SetMyLocationActivity::class.java)
            } else if(callingActivity == 2){
                // 회원가입할때
                // var intent = Intent(this, MainActivity::class.java)
            }
            val fullAddress = addressAdapter.getItem(position).toString()
            intent.putExtra("fullAddress", fullAddress)
            setResult(RESULT_OK, intent)
            finish()
        }
    }

    private fun getLocation(){
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
            && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestLocationPermission()
            return
        }
        Log.d(TAG, "getLocation: 실행")

        var fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        fusedLocationClient.lastLocation
            .addOnSuccessListener { location: Location? ->
                if (location != null) {
                    var geocoder = Geocoder(this, Locale.KOREAN)
                    var list: List<Address> = geocoder.getFromLocation(37.382698999999995, 127.11890570000001, 20)
                    Log.e(TAG, "list 내용: " + list.toString())
                    Log.e(TAG, "list.size: " + list.size)


                    var list2: List<Address> = geocoder.getFromLocationName("분당구",
                            10,
                            1.1,
                            0.1,
                            0.1,
                            0.1)
                    Log.e(TAG, "list 내용: " + list2.toString())
                    Log.e(TAG, "list.size: " + list2.size)
//                    // 풀네임 주소, 동 주소 두개
//                    val tempSet4 = getLocationInRange(3, location)
//                    val tempSet3 = getLocationInRange(2, location)
//                    val tempSet2 = getLocationInRange(1, location)
//                    val tempSet1 = getLocationInRange(0, location)
//
//                    tempSet4.removeAll(tempSet3)
//                    tempSet3.removeAll(tempSet2)
//                    tempSet2.removeAll(tempSet1)
//
//                    val addressList: Array<List<String>?> = arrayOfNulls<List<String>>(4)
//
//                    addressList[0] = tempSet1.toList()
//                    addressList[1] = addressList[0]!! + tempSet2
//                    addressList[2] = addressList[1]!! + tempSet3
//                    addressList[3] = addressList[2]!! + tempSet4
//
//                    setAdapter(addressList[3] as ArrayList<String>)
                }
            }
    }

    private fun getLocationInRange(range: Int, location: Location): TreeSet<String> {
        val length = 0.006
        val latitude: Double = round(location.latitude * 1000) /1000
        val longitude: Double = round(location.longitude * 1000) /1000
        var geocoder = Geocoder(this, Locale.KOREAN)
        var address: TreeSet<String> = sortedSetOf()
        var list: List<Address>
        for(i in -range..range){
            for(k in -range..range){
                list = geocoder.getFromLocation(latitude + i * length, longitude + k * length, 1)
                if(list.isNotEmpty()
                    && list[0].adminArea != null
                    && list[0].locality != null
                    && list[0].subLocality != null
                    && list[0].thoroughfare != null){
                    // address.add(list[0].thoroughfare)
                    address.add(list[0].adminArea + " " + list[0].locality + " " + list[0].subLocality + " " + list[0].thoroughfare)
                }
            }
        }
        return address
    }

    private fun requestLocationPermission(){
        if(ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_COARSE_LOCATION) &&
            ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)){
            // 권한 요구
            Log.d(TAG, "requestLoationPermission: 권한 요청")
            ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION),
                    PERMISSION_REQUEST_LOCATION)
        } else {
            // 다시 묻지 않음을 누르고 거부하고 요청했을떄
            Log.d(TAG, "requestLoationPermission: 다시 묻지 않음을 누르고 거부 후, 권한 요청")
            AlertDialog.Builder(this)
                .setTitle("알림")
                .setMessage("GPS 권한이 거부되었다네. 사용을 원한다면 설정에서 해당 권한을 직접 허용하게.")
                .setNeutralButton("설정", object : DialogInterface.OnClickListener {
                    override fun onClick(dialogInterface: DialogInterface, i: Int) {
                        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                        intent.setData(Uri.parse("package:" + getPackageName()))
                        startActivity(intent)
                    }
                })
                .setPositiveButton("확인", object : DialogInterface.OnClickListener {
                    override fun onClick(dialogInterface: DialogInterface, i: Int) {
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
                // 권한 요쳥 수락
                Log.d(TAG, "onRequestPermissionsResult: 권한 요청 수락")
                // 코드 실행
            } else {
                Log.d(TAG, "onRequestPermissionsResult: 권한 요청 거부")
                // 권한 요청 거부
            }
        }
    }

}