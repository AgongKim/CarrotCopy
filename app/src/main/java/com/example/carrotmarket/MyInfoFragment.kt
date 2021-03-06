package com.example.carrotmarket

import android.app.Activity
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
import com.example.carrotmarket.helper.MyApplication
import com.google.firebase.database.core.Context
import kotlinx.android.synthetic.main.fragment_my_info.*
import kotlinx.android.synthetic.main.fragment_my_info.view.*
import kotlinx.android.synthetic.main.msg_items.view.*


class MyInfoFragment() : Fragment() {
    var CHK_LOGIN = false
    lateinit var rootView :View

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle?): View? {
        rootView= inflater.inflate(R.layout.fragment_my_info, container, false)
        return rootView
    }

    override fun onResume() {
        super.onResume()
        CHK_LOGIN = MyApplication.prefs.login

        //기본프사설정
        if(CHK_LOGIN==false){
            rootView.member_photo.setImageResource(R.drawable.profile)
            var temp = rootView.member_info
            val layoutLogin:LinearLayout = layoutInflater.inflate(R.layout.msg_items,rootView as ViewGroup,false) as LinearLayout
            var textLogin = layoutLogin.txtMsg
            layoutLogin.removeAllViews()

            textLogin.text = "로그인하세요"
            textLogin.setTextColor(Color.parseColor("#FF6200EE"))
            textLogin.textSize = 20f

            //안에 뷰 삭제하고 "로그인하세요" 띄우기
            temp.removeAllViews()
            temp.addView(textLogin)
        }
        else {
            //여기에 회원 프사 불러다가 넣기
        }

        val setLocateButton = rootView.findViewById<Button>(R.id.btn_setLocate)

        setLocateButton.setOnClickListener {
            val intent: Intent = Intent(context, SetMyLocateActivity::class.java)
            startActivity(intent)
        }
        button4.setOnClickListener{
            MyApplication.prefs.login = false
            val mainActivity = activity as MainActivity
            mainActivity.CHK_LOGIN =false
            mainActivity.position=1
            mainActivity.setFrag()
        }

    }
}