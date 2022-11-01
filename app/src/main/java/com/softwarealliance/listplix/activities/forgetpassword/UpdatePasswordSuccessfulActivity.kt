package com.softwarealliance.listplix.activities.forgetpassword

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import com.softwarealliance.listplix.R
import com.softwarealliance.listplix.activities.IntroActivity

class UpdatePasswordSuccessfulActivity : AppCompatActivity() {
    lateinit var btn_login:AppCompatButton
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update_password_successful)

        btn_login = findViewById(R.id.btn_login)
        btn_login.setOnClickListener {
            startActivity(Intent(this@UpdatePasswordSuccessfulActivity,IntroActivity::class.java))
            finish();
        }
    }
}