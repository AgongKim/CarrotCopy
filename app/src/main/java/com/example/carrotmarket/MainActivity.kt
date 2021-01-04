package com.example.carrotmarket

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.LinearLayout
import androidx.fragment.app.FragmentManager
import com.example.carrotmarket.adapter.RecyclerAdapterHome
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setFrag(1)
    }

    fun setFrag(n:Int){
        if(n==1){
            val tran = supportFragmentManager.beginTransaction()
            tran.replace(R.id.fragment,HomeFragment())
            tran.addToBackStack(null)
            tran.commit()
        }
    }
}
