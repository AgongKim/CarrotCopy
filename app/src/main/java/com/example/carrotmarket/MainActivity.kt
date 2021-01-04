package com.example.carrotmarket

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import androidx.fragment.app.FragmentManager
import com.example.carrotmarket.adapter.RecyclerAdapterHome
import kotlinx.android.synthetic.main.activity_main.*

class
MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setFrag(1)

        btnHome.setOnClickListener(View.OnClickListener {
            setFrag(1)
        })
        btnChat.setOnClickListener(View.OnClickListener {
            setFrag(2)
        })
        btnMypage.setOnClickListener(View.OnClickListener {
            setFrag(4)
        })
    }

    fun setFrag(n:Int){
        if(n==1){
            val tran = supportFragmentManager.beginTransaction()
            tran.replace(R.id.fragment,HomeFragment())
            tran.addToBackStack(null)
            tran.commit()
        }else if(n==2){
            val tran = supportFragmentManager.beginTransaction()
            tran.replace(R.id.fragment,ChatFragment())
            tran.addToBackStack(null)
            tran.commit()
        }else if(n==4){
            val tran = supportFragmentManager.beginTransaction()
            tran.replace(R.id.fragment,MyInfoFragment())
            tran.addToBackStack(null)
            tran.commit()
        }
    }
}
