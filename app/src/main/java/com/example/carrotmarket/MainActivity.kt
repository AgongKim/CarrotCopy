package com.example.carrotmarket

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import androidx.fragment.app.FragmentManager
import com.example.carrotmarket.adapter.RecyclerAdapterHome
import kotlinx.android.synthetic.main.activity_main.*

var position = 1

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setFrag()

        btnHome.setOnClickListener(View.OnClickListener {
            position = 1
            setFrag()
        })
        btnChat.setOnClickListener(View.OnClickListener {
            position = 3
            setFrag()
        })
        btnMypage.setOnClickListener(View.OnClickListener {
            position = 4
            setFrag()
        })
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
}
