package com.example.carrotmarket

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.FragmentManager
import com.example.carrotmarket.adapter.RecyclerAdapterHome
import com.example.carrotmarket.helper.MyApplication
import com.example.carrotmarket.helper.MySharedPreferences
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.login_popup.view.*



class MainActivity() : AppCompatActivity() {
    var position = 1
    var CHK_LOGIN = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)



        setFrag()

        btnHome.setOnClickListener {
            position = 1
            setFrag()
        }
        btnChat.setOnClickListener {
            if(CHK_LOGIN==true){
                position = 3
                setFrag()
            }else{
                //로그인 안되어있으면 채팅 x 창띄우기
                showLoginPopup()
            }
        }
        btnMypage.setOnClickListener {
            position = 4
            setFrag()
        }

    }

    override fun onResume() {
        super.onResume()
        //자동로그인 & 로그인된 회원정보 공유용 쉐어드 프리퍼런스\
        CHK_LOGIN = MyApplication.prefs.login

        //로그인 한적이있으면 멤버정보 저장
        if(CHK_LOGIN){
            //여기에 맴버정보 저장

        }
    }

    fun setFrag(){
        if(position==1){
            val tran = supportFragmentManager.beginTransaction()
            tran.replace(R.id.fragment,HomeFragment())
            tran.addToBackStack(null)
            tran.commit()
        }else if(position==3){
            val tran = supportFragmentManager.beginTransaction()
            tran.replace(R.id.fragment,ChatFragment())
            tran.addToBackStack(null)
            tran.commit()
        }else if(position==4){
            val tran = supportFragmentManager.beginTransaction()
            tran.replace(R.id.fragment,MyInfoFragment())
            tran.addToBackStack(null)
            tran.commit()
        }
    }

    fun showLoginPopup(){
        val inflater = getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view = inflater.inflate(R.layout.login_popup,null)
        val textView = view.textView
        
        val alertDialog = AlertDialog.Builder(this)
                .setPositiveButton("로그인/가입"){dialog, which ->  
                    //로그인창으로 넘기기
                    val intent: Intent = Intent(this, LoginActivity::class.java)
                    startActivity(intent)
                }
                .setNegativeButton("취소",null)
                .create()
        alertDialog.setView(view)
        alertDialog.show()
    }
}
