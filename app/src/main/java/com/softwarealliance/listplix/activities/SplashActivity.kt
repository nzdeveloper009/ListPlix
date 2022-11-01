package com.softwarealliance.listplix.activities

import android.content.Intent
import android.graphics.Typeface
import android.os.Bundle
import android.os.Handler
import android.view.WindowManager
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.softwarealliance.listplix.R

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        // This is used to hide the status bar and make the splash screen as a full screen activity.
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        val textView = findViewById<TextView>(R.id.tv_app_name)

        // This is used to get the file from the assets folder and set it to the title textView.
        val typeface: Typeface =
            Typeface.createFromAsset(assets, "carbon bl.ttf")
        textView.typeface = typeface

/*        val paint = textView.paint
        val width = paint.measureText(textView.text.toString())
        val textShader: Shader = LinearGradient(0f, 0f, width, textView.textSize, intArrayOf(
            Color.parseColor("#F97C3C"),
            Color.parseColor("#FDB54E"),
            *//*Color.parseColor("#64B678"),
            Color.parseColor("#478AEA"),*//*
            Color.parseColor("#8446CC")
        ), null, Shader.TileMode.REPEAT)

        textView.paint.setShader(textShader)*/

        // Adding the handler to after the a task after some delay.
        Handler().postDelayed({
            startActivity(Intent(this, IntroActivity::class.java))
        },2000)

    }
}