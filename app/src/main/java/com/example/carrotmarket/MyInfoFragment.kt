package com.example.carrotmarket

import android.content.Intent
import android.graphics.Color
import android.nfc.Tag
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import kotlinx.android.synthetic.main.fragment_my_info.*
import kotlinx.android.synthetic.main.fragment_my_info.view.*
import kotlinx.android.synthetic.main.msg_items.view.*


class MyInfoFragment(var CHK_LOGIN:Boolean) : Fragment() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle?): View? {
        val view: View = inflater.inflate(R.layout.fragment_my_info, container, false)

        //기본프사설정
        if(CHK_LOGIN==false){
            view.member_photo.setImageResource(R.drawable.profile)
            var temp = view.member_info
            val layoutLogin:LinearLayout = inflater.inflate(R.layout.msg_items,container,false) as LinearLayout
            var textLogin = layoutLogin.txtMsg
            layoutLogin.removeAllViews()

            textLogin.setText("로그인하세요")
            textLogin.setTextColor(Color.parseColor("#FF6200EE"))
            textLogin.setTextSize(20f)

            //안에 뷰 삭제하고 "로그인하세요" 띄우기
            temp.removeAllViews()
            temp.addView(textLogin)
        }
        else {
            //여기에 회원 프사 불러다가 넣기
        }

        val setLocateButton = view.findViewById<Button>(R.id.btn_setLocate)

        setLocateButton.setOnClickListener {
            val intent: Intent = Intent(context, SetMyLocateActivity::class.java)
            startActivity(intent)
        }
        return view
    }

}