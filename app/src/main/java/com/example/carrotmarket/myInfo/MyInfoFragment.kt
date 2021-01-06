package com.example.carrotmarket.myInfo

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.example.carrotmarket.R


class MyInfoFragment : Fragment() {



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle?): View? {
        val view: View = inflater.inflate(R.layout.fragment_my_info, container, false)

        val setLocateButton = view.findViewById<Button>(R.id.btn_setLocate)

        setLocateButton.setOnClickListener {
            val intent: Intent = Intent(context, SetMyLocationActivity::class.java)
            startActivity(intent)
        }
        return view
    }

}