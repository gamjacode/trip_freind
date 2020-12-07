package com.example.trip_freind

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.trip_freind.R
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {
    var auth: FirebaseAuth? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        auth = FirebaseAuth.getInstance()

        login_button.setOnClickListener(){
            loginEmail()
        }
    }
    fun loginEmail(){
        auth?.signInWithEmailAndPassword(login_email_input.text.toString(),login_password_input.text.toString())
            ?.addOnCompleteListener {
                if(it.isSuccessful){
                    startActivity(Intent(this, HomeActivity::class.java))
                }else{
                    Toast.makeText(this,"아이디 또는 비밀번호를 확인하세요", Toast.LENGTH_LONG).show()
                }
            }
    }
}
