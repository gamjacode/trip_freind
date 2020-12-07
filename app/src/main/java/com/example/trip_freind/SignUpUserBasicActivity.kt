package com.example.trip_freind

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.trip_freind.R
import kotlinx.android.synthetic.main.activity_sign_up_user_basic.*

class SignUpUserBasicActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up_user_basic)

        sign_up_second_next_button.setOnClickListener() {
            val inputName = sign_up_name_input.text.toString()
            val inputPhone = sign_up_phone_number_input.text.toString()
            val email = intent.getStringExtra("email")
            val uid = intent.getStringExtra("uid")

            val intent = Intent(this, SignUpUserDetailActivity::class.java)
            intent.putExtra("name", inputName)
            intent.putExtra("phone", inputPhone)
            intent.putExtra("email", email)
            intent.putExtra("uid", uid)
            startActivity(intent)
        }
    }
}
