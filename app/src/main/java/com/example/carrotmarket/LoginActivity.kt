package com.example.carrotmarket

import android.os.Bundle
import android.telephony.PhoneNumberFormattingTextWatcher
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.loopj.android.http.AsyncHttpResponseHandler
import cz.msebera.android.httpclient.Header
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity(),View.OnClickListener {
    //private var client = object : AsyncHttpClient(){}
    //private lateinit var response:AsyncHttpResponseHandler
    private var phoneNum = ""
    private var verificationInProgress = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        //에딧텍스트 변화 감지
        editTextPhone.addTextChangedListener(MyEditWatcher())

        //http통신 콜백 선언
        //response = SMSCallback()
        //client.addHeader("")
        //var parameters = RequestParams()
        btnPhone.setOnClickListener(this)
        btnCode.setOnClickListener(this)

    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.btnPhone -> {
                //인증번호 전송!
                Toast.makeText(this,"메세지가 전송되었습니다 인증번호를 입력해주세요",Toast.LENGTH_SHORT).show()
            }
            R.id.btnCode -> {
                //인증번호 확인!
                if(phoneNum.equals("01026166347")||phoneNum.equals("01088977064")){
                    //로그인화면

                }else {
                    //회원가입화면으로 이동

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
            if(number.length>=12){
                btnPhone.isEnabled
                btnCode.isEnabled
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
    }
    //다시 실행되었을 때 진행중인지 아닌지 저장된값 가져온다.
    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        verificationInProgress = savedInstanceState.getBoolean(KEY_VERIFY_IN_PROGRESS)
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
    }
}