package com.example.trip_freind

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.example.trip_freind.R
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_sign_up_email_and_password.*

class SignUpEmailAndPasswordActivity : AppCompatActivity() {
    var auth: FirebaseAuth? = null
  
    lateinit var email: String
    lateinit var pw1: String
    lateinit var pw2: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up_email_and_password)

        //이메일 회원가입 시키기 전 로딩바가 보이지 않게 설정
        sign_up_create_email_loading_progress_bar.visibility = View.GONE //INVISIBLE 아님?

        auth = FirebaseAuth.getInstance()   //auth : 회원가입에 관련된 정보들을 쓸 수 있게해줌
        sign_up_first_next_button.setOnClickListener() {
            email = sign_up_email_input.text.toString()
            pw1 = sign_up_password_input.text.toString()
            pw2 = sign_up_password_check_input.text.toString()
            if (pw1 != pw2) {
                Toast.makeText(this, "비밀번호가 일치하지 않습니다.", Toast.LENGTH_LONG).show()
            } else if (email.isEmpty() || pw1.isEmpty()) {
                Toast.makeText(this, "이메일 또는 비밀번호를 확인해주세요.", Toast.LENGTH_SHORT).show()
            } else {
                sign_up_create_email_loading_progress_bar.visibility = View.VISIBLE
                createEmail()
            }
        }
    }

    private fun createEmail() {
        auth?.createUserWithEmailAndPassword(
            sign_up_email_input.text.toString(),
            sign_up_password_input.text.toString()
        )
            ?.addOnCompleteListener {
                if (it.isSuccessful) {
                    sign_up_create_email_loading_progress_bar.visibility = View.GONE
                    val intent = Intent(this, SignUpUserBasicActivity::class.java)
                    email = sign_up_email_input.text.toString()
                    pw1 = sign_up_password_input.text.toString()
                    intent.putExtra("email", email)
                    intent.putExtra("pw", pw1)
                    intent.putExtra("uid", it.result!!.user!!.uid)
                    startActivity(intent)
                } else if (it.exception.toString().isNotEmpty()) {
                    sign_up_create_email_loading_progress_bar.visibility = View.GONE
                    Toast.makeText(this, it.exception.toString(), Toast.LENGTH_SHORT).show()
                }
            }
    }
}
