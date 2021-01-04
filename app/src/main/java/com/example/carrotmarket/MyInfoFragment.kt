package com.example.carrotmarket

import android.content.Intent
import android.nfc.Tag
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import kotlinx.android.synthetic.main.fragment_my_info.*

class MyInfoFragment : Fragment() {
    private var TAG: String = "[MyInfoFragment]"
    private lateinit var setLocateButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle?): View? {
        val view: View = inflater.inflate(R.layout.fragment_my_info, container, false)
        setLocateButton = view.findViewById(R.id.btn_setLocate)
        setLocateButton.setOnClickListener {
            val intent: Intent = Intent(context, SetMyLocateActivity::class.java)
            startActivity(intent)
            Log.d(TAG, "onCreateView: Clicked")
        }
        return view
    }

}