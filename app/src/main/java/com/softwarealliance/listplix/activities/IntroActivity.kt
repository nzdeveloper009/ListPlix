package com.softwarealliance.listplix.activities

import android.content.Intent
import android.graphics.Typeface
import android.os.Bundle
import android.view.WindowManager
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import com.softwarealliance.listplix.R

class IntroActivity : AppCompatActivity() {

    private lateinit var btn_sign_in_intro: AppCompatButton
    private lateinit var btn_sign_up_intro: AppCompatButton
    private lateinit var tv_app_name_intro: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        //This call the parent constructor
        super.onCreate(savedInstanceState)
        // This is used to align the xml view to this class
        setContentView(R.layout.activity_intro)

        // This is used to hide the status bar and make the splash screen as a full screen activity.
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

        initView()
        listeners()



    }

    private fun listeners() {
        btn_sign_in_intro.setOnClickListener {

            // Launch the sign in screen.
            startActivity(Intent(this@IntroActivity, SignInActivity::class.java))
        }

        btn_sign_up_intro.setOnClickListener {

            // Launch the sign up screen.
            startActivity(Intent(this@IntroActivity, SignUpActivity::class.java))
        }
    }

    private fun initView() {
        btn_sign_in_intro = findViewById(R.id.btn_sign_in_intro)
        btn_sign_up_intro = findViewById(R.id.btn_sign_up_intro)
        tv_app_name_intro = findViewById(R.id.tv_app_name_intro)


        // This is used to get the file from the assets folder and set it to the title textView.
        val typeface: Typeface =
            Typeface.createFromAsset(assets, "carbon bl.ttf")
        tv_app_name_intro.typeface = typeface
    }
}