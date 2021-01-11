package com.example.carrotmarket

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import com.example.carrotmarket.myInfo.MyInfoFragment
import android.widget.LinearLayout
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.FragmentManager
import com.example.carrotmarket.adapter.RecyclerAdapterHome
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.login_popup.view.*


var position = 4

class MainActivity() : AppCompatActivity() {

    var CHK_LOGIN =false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if(false){
            //여기에 자동로그인 구현하기
        }else{
            CHK_LOGIN =false
        }

        setFrag()

        btnHome.setOnClickListener(View.OnClickListener {
            position = 1
            setFrag()
        })
        btnChat.setOnClickListener(View.OnClickListener {
            if(CHK_LOGIN==true){
                position = 3
                setFrag()
            }else{
                //로그인 안되어있으면 채팅 x 창띄우기
                showLoginPopup()
            }
        })
        btnMypage.setOnClickListener(View.OnClickListener {
            position = 4
            setFrag()
        })
    }

    fun setFrag(){
        if(position==1){
            val tran = supportFragmentManager.beginTransaction()
            tran.replace(R.id.fragment,HomeFragment(CHK_LOGIN))
            tran.addToBackStack(null)
            tran.commit()
        }else if(position==3){
            val tran = supportFragmentManager.beginTransaction()
            tran.replace(R.id.fragment,ChatFragment(CHK_LOGIN))
            tran.addToBackStack(null)
            tran.commit()
        }else if(position==4){
            val tran = supportFragmentManager.beginTransaction()
            tran.replace(R.id.fragment,MyInfoFragment(CHK_LOGIN))
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
                }
                .setNegativeButton("취소",null)
                .create()
        alertDialog.setView(view)
        alertDialog.show()
    }
}
