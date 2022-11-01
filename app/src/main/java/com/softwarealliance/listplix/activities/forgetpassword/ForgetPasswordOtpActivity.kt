package com.softwarealliance.listplix.activities.forgetpassword

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.Toolbar
import com.chaos.view.PinView
import com.softwarealliance.listplix.R
import com.softwarealliance.listplix.activities.BaseActivity

class ForgetPasswordOtpActivity : BaseActivity() {

    lateinit var email: String
    lateinit var otpcode: String
    lateinit var user_entered_code: String
    lateinit var forget_password_next_btn: AppCompatButton
    lateinit var forget_password_description: TextView
    lateinit var pinView: PinView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forget_password_otp)

        // This is used to hide the status bar and make the splash screen as a full screen activity.
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )


        val bundle: Bundle = intent.extras!!
        otpcode = bundle.get("code") as String
        email = bundle.get("email") as String

        setupActionBar()
        initView()
        mListeners()
    }

    private fun mListeners() {

        pinView.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (p0.toString().trim() == otpcode) {
                    user_entered_code = p0.toString().trim()
                    showProgressDialog(resources.getString(R.string.please_wait))
                    verificationSuccessful()
                } else {
                    user_entered_code = ""
                    showErrorSnackBar("Code Does Not Matched!!")
                }
            }

            override fun afterTextChanged(p0: Editable?) {

            }

        })
        forget_password_next_btn.setOnClickListener {
            showProgressDialog(resources.getString(R.string.please_wait))
            if(user_entered_code.isEmpty())
            {
                showErrorSnackBar("Please Enter OTP")
                hideProgressDialog()
                return@setOnClickListener
            } else if (user_entered_code.equals("")) {
                showErrorSnackBar("Code Does Not Matched!!")
                hideProgressDialog()
                return@setOnClickListener
            }
            verifyOtp(user_entered_code)
        }
    }

    private fun verificationSuccessful() {

        hideProgressDialog()
        Toast.makeText(this@ForgetPasswordOtpActivity,"OTP Verification Successful! Now Set your Password",Toast.LENGTH_LONG).show()
        val intent = Intent(this@ForgetPasswordOtpActivity,ForgetPasswordSetCredentialActivity::class.java)
        intent.putExtra("email",email)
        startActivity(intent)
        finish()
    }

    private fun verifyOtp(s: String) {
        if(s == otpcode)
        {
            verificationSuccessful()
        } else {
            showErrorSnackBar("Code Does Not Matched!!")
            hideProgressDialog()
        }
    }

    private fun initView() {
        pinView = findViewById(R.id.pin_view)
        forget_password_next_btn = findViewById(R.id.forget_password_next_btn)
        forget_password_description = findViewById(R.id.forget_password_description)

        val str_email_desc = forget_password_description.text.toString() + " " + email
        forget_password_description.text = str_email_desc

        pinView.requestFocus()
        val inputMethodManager: InputMethodManager =
            getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.toggleSoftInput(
            InputMethodManager.SHOW_FORCED,
            InputMethodManager.HIDE_IMPLICIT_ONLY
        )

    }

    private fun setupActionBar() {
        val toolbar_forget_password_otp_activity: Toolbar =
            findViewById(R.id.toolbar_forget_password_otp_activity)

        setSupportActionBar(toolbar_forget_password_otp_activity)

        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_black_color_back_24dp)
        }

        toolbar_forget_password_otp_activity.setNavigationOnClickListener { onBackPressed() }

    }
}