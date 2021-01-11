package com.example.carrotmarket

import android.app.Activity
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.telephony.PhoneNumberFormattingTextWatcher
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.carrotmarket.helper.MyApplication
import com.loopj.android.http.AsyncHttpResponseHandler
import cz.msebera.android.httpclient.Header
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.msg_items.view.*
import java.util.*

class LoginActivity : AppCompatActivity(),View.OnClickListener {
    //private var client = object : AsyncHttpClient(){}
    //private lateinit var response:AsyncHttpResponseHandler
    private var phoneNum = ""
    private var verificationInProgress = false
    private lateinit var layout_phone : LinearLayout

    //인증시간 디폴트 300초
    private var time_left = 300
    private var timerTask : Timer? =null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        layout_phone = findViewById(R.id.layout_phone)

        //에딧텍스트 변화 감지
        editTextPhone.addTextChangedListener(MyEditWatcher())

        //http통신 콜백 선언
        //response = SMSCallback()
        //client.addHeader("")
        //var parameters = RequestParams()
        btnPhone.setOnClickListener(this)
        btnCode.setOnClickListener(this)

        layout_phone.visibility = View.VISIBLE
        layout_code.visibility = View.GONE
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.btnPhone -> {
                if(validatePhoneNumber()){
                    layout_phone.visibility = View.GONE
                    layout_code.visibility = View.VISIBLE

                    //인증번호 전송!
                    Toast.makeText(this,"메세지가 전송되었습니다 인증번호를 입력해주세요",Toast.LENGTH_SHORT).show()
                    editTextPhone.isEnabled = false
                    btnPhone.isEnabled = false
                    //인증진행중 플래그
                    verificationInProgress =true

                    //인증버튼 사라지고 남은시간 보여줌
                    layout_phone.removeAllViews()
                    var temp = layoutInflater.inflate(R.layout.msg_items,findViewById(R.id.rootView),false) as LinearLayout
                    var textView = temp.txtMsg
                    temp.removeAllViews()
                    layout_phone.addView(textView)

                    //타이머 동작
                    timerTask = kotlin.concurrent.timer(period = 1000){
                        time_left-- //남은시간 감소
                        if(time_left>0){
                            runOnUiThread(){
                                textView.text="남은시간 : $time_left"
                            }
                        }else{
                            //인증 타임아웃
                            cancel()
                            verificationInProgress = false
                        }
                    }
                }else{
                    Toast.makeText(this,"폰번호를 확인해주세요",Toast.LENGTH_SHORT).show()
                }
            }
            R.id.btnCode -> {

                if(editTextCode.text.toString().equals("111111")){
                    verificationInProgress =false
                    timerTask?.cancel()

                    //인증번호 확인!
                    if(phoneNum.equals("01026166347")||phoneNum.equals("01088977064")){
                        //로그인화면
                        MyApplication.prefs.login = true
                        this.finish()
                    }else {
                        //회원가입화면으로 이동
                        val intent: Intent = Intent(MyApplication.applicationContext(), JoinActivity::class.java)
                        startActivity(intent)
                    }
                }else{
                    Toast.makeText(this,"인증번호 오류!",Toast.LENGTH_SHORT).show()
                }

            }
        }
    }



    //[에딧텍스트리스너 구현]
    //전화번호 010-xxxx-xxxx형태로 만들어주고 10자리이상일때 버튼 활성화
    inner class MyEditWatcher() : PhoneNumberFormattingTextWatcher(){
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            super.onTextChanged(s, start, before, count)
            var number = s.toString()

            if(number.length>=13){
                btnCode.isEnabled = true
                btnPhone.isEnabled = true
            }else{
                btnCode.isEnabled = false
                btnPhone.isEnabled = false
            }
            phoneNum = s.toString().replace("-","")
        }
    }
    //[에딧텍스트리스너 종료]

    //[문자 송신 콜백 구현]
    inner class SMSCallback():AsyncHttpResponseHandler(){
        override fun onSuccess(statusCode: Int, headers: Array<out Header>?, responseBody: ByteArray?) {
            Log.d(TAG, "통신 성공 $responseBody")
            verificationInProgress = false
            //인증코드 입력하도록 화면 변경 이곳에 구현
        }

        override fun onFailure(statusCode: Int, headers: Array<out Header>?, responseBody: ByteArray?, error: Throwable?) {
            Log.d(TAG, "통신 실패 $responseBody")
            verificationInProgress = false
        }
    }
    //[인증콜백 종료]

    //생성될때 인증이 진행중인지 아닌지 상태 저장한다.
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putBoolean(KEY_VERIFY_IN_PROGRESS, verificationInProgress)
        outState.putInt(KEY_VERIFY_TIME_LEFT,time_left)
    }
    //다시 실행되었을 때 진행중인지 아닌지 저장된값 가져온다.
    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        verificationInProgress = savedInstanceState.getBoolean(KEY_VERIFY_IN_PROGRESS)
        time_left = savedInstanceState.getInt(KEY_VERIFY_TIME_LEFT)
    }

    //폰번호 유효성 검사
    private fun validatePhoneNumber(): Boolean {
        if (TextUtils.isEmpty(phoneNum)) {
            Toast.makeText(this,"전화번호 형식오류",Toast.LENGTH_SHORT).show()
            return false
        }else if(phoneNum.length<10){
            Toast.makeText(this,"전화번호 형식오류",Toast.LENGTH_SHORT).show()
            return false
        }
        return true
    }

    companion object {
        private const val TAG = "PhoneAuthActivity"
        private const val KEY_VERIFY_IN_PROGRESS = "key_verify_in_progress"
        private const val KEY_VERIFY_TIME_LEFT = "key_verify_time_left"
    }
}