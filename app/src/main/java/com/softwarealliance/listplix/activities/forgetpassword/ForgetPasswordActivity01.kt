package com.softwarealliance.listplix.activities.forgetpassword

import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.view.WindowManager
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.Toolbar
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.softwarealliance.listplix.R
import com.softwarealliance.listplix.activities.BaseActivity
import com.softwarealliance.listplix.api.ApiClient
import com.softwarealliance.listplix.model.responseapi.ResponseForgetPassword
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ForgetPasswordActivity01 : BaseActivity() {

    private lateinit var et_email: TextInputEditText
    private lateinit var til_email: TextInputLayout
    private lateinit var forget_password_next_btn: AppCompatButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forget_password01)

        // This is used to hide the status bar and make the splash screen as a full screen activity.
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

        setupActionBar()
        initView()
        mListeners()
    }

    private fun setStrokeColorState(): ColorStateList {
        //Color from hex string
        val color = Color.parseColor("#0591F8")
        val states = arrayOf(
            intArrayOf(android.R.attr.state_focused),
            intArrayOf(android.R.attr.state_hovered),
            intArrayOf(android.R.attr.state_enabled),
            intArrayOf()
        )
        val colors = intArrayOf(
            color,
            color,
            color,
            color
        )
        return ColorStateList(states, colors)
    }

    private fun mListeners() {
        forget_password_next_btn.setOnClickListener {
            if (et_email.text.toString().isEmpty()) {
                til_email.setError("Required Field!")
                et_email.requestFocus()
                return@setOnClickListener
            } else {
                til_email.setErrorEnabled(false)
                showProgressDialog(resources.getString(R.string.please_wait))
                val apiClient = ApiClient()
                apiClient.getApiService(this).requestResetPassword(et_email.text.toString()).enqueue(
                    object : Callback<ResponseForgetPassword> {
                        override fun onResponse(
                            call: Call<ResponseForgetPassword>,
                            response: Response<ResponseForgetPassword>
                        ) {
                            if (response.isSuccessful) {
                                val intent = Intent(
                                    this@ForgetPasswordActivity01,
                                    ForgetPasswordOtpActivity::class.java
                                )
                                val code = response.body()?.code?:null
                                intent.putExtra("email",et_email.text.toString())
                                intent.putExtra("code",code)
                                startActivity(intent)
                                finish()
                            } else {
                                showErrorSnackBar(response.errorBody().toString())
                            }
                            hideProgressDialog()
                        }

                        override fun onFailure(call: Call<ResponseForgetPassword>, t: Throwable) {
                            showErrorSnackBar(t.message.toString())
                            hideProgressDialog()
                        }

                    }
                )
            }
        }
    }

    private fun initView() {
        et_email = findViewById(R.id.et_email)
        til_email = findViewById(R.id.til_email)
        forget_password_next_btn = findViewById(R.id.forget_password_next_btn)

        til_email.setBoxStrokeColorStateList(setStrokeColorState())
    }

    private fun setupActionBar() {
        val toolbar_forget_password_activity: Toolbar =
            findViewById(R.id.toolbar_forget_password_activity)

        setSupportActionBar(toolbar_forget_password_activity)

        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_black_color_back_24dp)
        }

        toolbar_forget_password_activity.setNavigationOnClickListener { onBackPressed() }
    }
}