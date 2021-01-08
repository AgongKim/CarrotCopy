package com.example.carrotmarket

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.telephony.PhoneNumberFormattingTextWatcher
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import com.example.carrotmarket.helper.MyApplication
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.*
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_login.*
import java.util.concurrent.TimeUnit

class LoginActivity : AppCompatActivity(),View.OnClickListener {
    private lateinit var auth: FirebaseAuth
    private var phoneNum =""
    private lateinit var callbacks :PhoneAuthProvider.OnVerificationStateChangedCallbacks
    private var verificationInProgress = false
    private var storedVerificationId: String? = ""
    private lateinit var resendToken: PhoneAuthProvider.ForceResendingToken
    val TAG = "[TEST]"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        //init _auth
        auth = Firebase.auth
        auth.setLanguageCode("kr")

        //에딧텍스트 변화 감지
        var editText = findViewById(R.id.editTextPhone) as EditText
        var button = findViewById(R.id.btnPhone) as Button
        editText.addTextChangedListener(MyEditWatcher(phoneNum,button))

        //인증 콜백 init
        callbacks =object :PhoneAuthProvider.OnVerificationStateChangedCallbacks(){
            override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                Log.d(TAG, "onVerificationCompleted:$credential")
                verificationInProgress = false

                // Update the UI and attempt sign in with the phone credential
                //updateUI(STATE_VERIFY_SUCCESS, credential)
                //signInWithPhoneAuthCredential(credential)
            }

            override fun onVerificationFailed(e: FirebaseException) {
                Log.w(TAG, "onVerificationFailed", e)
                verificationInProgress = false

                if (e is FirebaseAuthInvalidCredentialsException) {
                    Toast.makeText(MyApplication.applicationContext(),"잘못된 번호입니다",Toast.LENGTH_SHORT).show()
                } else if (e is FirebaseTooManyRequestsException) {
                    //인증 한도초과
                }
                //updateUI(STATE_VERIFY_FAILED)
            }
            override fun onCodeSent(
                verificationId: String,
                token: PhoneAuthProvider.ForceResendingToken
            ) {
                // 인증코드가 보내졌으므로 코드 입력하도록 화면 변경
                Log.d(TAG, "onCodeSent:$verificationId")

                // 인증ID와 토큰 나중에 쓰도록 저장
                storedVerificationId = verificationId
                resendToken = token

                // Update UI
                //updateUI(STATE_CODE_SENT)
            }
        }//인증 콜백 종료


        //회원가입 버튼 리스너
       button.setOnClickListener{


        }

    }

    //전화번호 010-xxxx-xxxx형태로 만들어주고 10자리이상일때 버튼 활성화
    class MyEditWatcher(var phoneNum:String,var button: Button) : PhoneNumberFormattingTextWatcher(){
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            super.onTextChanged(s, start, before, count)
            var number = s.toString()
            button.isEnabled = number.length>=12
            phoneNum = s.toString().replace("-","")
        }
    }

    override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = auth.currentUser
        //updateUI(currentUser)

        if (verificationInProgress && validatePhoneNumber()) {
            startPhoneNumberVerification(phoneNum)
        }
    }

    private fun startPhoneNumberVerification(phoneNumber: String) {
        //verifyPhoneNumber 메서드는 재진입이 가능합니다.
        // 즉, 액티비티의 onStart 메서드와 같은 위치에서 verifyPhoneNumber 메서드를 여러 번 호출해도 원래 요청이 시간 초과되지 않았다면
        // SMS를 재차 보내지 않습니다. -->onResume에 넣어야할듯 메세지 앱 갔다가 올때
        val options = PhoneAuthOptions.newBuilder(auth)
            .setPhoneNumber(phoneNumber)       // Phone number to verify
            .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
            .setActivity(this)                 // Activity (for callback binding)
            .setCallbacks(callbacks)          // OnVerificationStateChangedCallbacks
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)

        verificationInProgress = true
    }

    private fun verifyPhoneNumberWithCode(verificationId: String?, code: String) {
        // [START verify_with_code]
        val credential = PhoneAuthProvider.getCredential(verificationId!!, code)
        // [END verify_with_code]
        signInWithPhoneAuthCredential(credential)
    }

    // 코드 재전송
    private fun resendVerificationCode(
        phoneNumber: String,
        token: PhoneAuthProvider.ForceResendingToken?
    ) {
        val optionsBuilder = PhoneAuthOptions.newBuilder(auth)
            .setPhoneNumber(phoneNumber)       // Phone number to verify
            .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
            .setActivity(this)                 // Activity (for callback binding)
            .setCallbacks(callbacks)          // OnVerificationStateChangedCallbacks
        if (token != null) {
            optionsBuilder.setForceResendingToken(token) // callback's ForceResendingToken
        }
        PhoneAuthProvider.verifyPhoneNumber(optionsBuilder.build())
    }
    // 코드재전송 끝

    // 핸드폰 인증 로그인 성공
    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    //로그인 성공!
                    Log.d(TAG, "signInWithCredential:success")

                    val user = task.result?.user
                    // [START_EXCLUDE]
                    //updateUI(STATE_SIGNIN_SUCCESS, user)
                    // [END_EXCLUDE]
                } else {
                    // 로그인 실패!
                    Log.w(TAG, "signInWithCredential:failure", task.exception)
                    if (task.exception is FirebaseAuthInvalidCredentialsException) {
                        // 인증코드오류
                        // [START_EXCLUDE silent]
                        // 로그인실패!
                        // [END_EXCLUDE]
                    }
                    // [START_EXCLUDE silent]
                    // Update UI
                    //updateUI(STATE_SIGNIN_FAILED)
                    // [END_EXCLUDE]
                }
            }
    }
    // [END sign_in_with_phone]

    //로그아웃
    private fun signOut() {
        auth.signOut()
        //updateUI(STATE_INITIALIZED)
    }
    //폰번호 유효성 검사
    private fun validatePhoneNumber(): Boolean {
        if (TextUtils.isEmpty(phoneNum)) {
            Toast.makeText(this,"전화번호 형식오류",Toast.LENGTH_SHORT).show()
            return false
        }
        return true
    }


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



    override fun onClick(v: View) {
        when (v.id) {
            //인증시작
            R.id.btnPhone -> {
                if (!validatePhoneNumber()) {
                    return
                }
                startPhoneNumberVerification(editTextPhone.text.toString())
            }
            //인증코드 입력완료
            R.id.btnCode -> {
                val code = editTextCode.text.toString()
                if (TextUtils.isEmpty(code)) {
                    Toast.makeText(this,"코드를 입력하시게",Toast.LENGTH_SHORT).show()
                    return
                }
                verifyPhoneNumberWithCode(storedVerificationId, code)
            }
//            //재전송
//            R.id.buttonResend -> resendVerificationCode(binding.fieldPhoneNumber.text.toString(), resendToken)
//            //로그아웃
//            R.id.signOutButton -> signOut()
        }
    }

    companion object {
        private const val TAG = "PhoneAuthActivity"
        private const val KEY_VERIFY_IN_PROGRESS = "key_verify_in_progress"
        private const val STATE_INITIALIZED = 1
        private const val STATE_VERIFY_FAILED = 3
        private const val STATE_VERIFY_SUCCESS = 4
        private const val STATE_CODE_SENT = 2
        private const val STATE_SIGNIN_FAILED = 5
        private const val STATE_SIGNIN_SUCCESS = 6
    }
}