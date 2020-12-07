package com.example.trip_freind

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.trip_freind.R
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        main_sign_up_button.setOnClickListener() {
            startActivity(Intent(this, SignUpEmailAndPasswordActivity::class.java))
        }

        main_login_button.setOnClickListener() {
            startActivity(Intent(this, LoginActivity::class.java))
        }

    }
}
