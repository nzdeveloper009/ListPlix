package com.softwarealliance.listplix.activities

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
import com.chaos.view.PinView
import com.softwarealliance.listplix.R
import com.softwarealliance.listplix.`interface`.APIListPlixJson
import com.softwarealliance.listplix.model.RequestOtpModel
import com.softwarealliance.listplix.responseapi.ResponseOtp
import com.softwarealliance.listplix.service.ServiceBuilder
import com.softwarealliance.listplix.utils.LocalStorage
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class VerifyOTPActivity : BaseActivity() {
    lateinit var pinView: PinView
    private lateinit var localStorage: LocalStorage
    private lateinit var resendCode: TextView
    lateinit var email: String
    lateinit var vcode: String
    lateinit var token: String
    lateinit var user_entered_code: String
    lateinit var btn_verify: AppCompatButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_verify_otp)

        // This is used to hide the status bar and make the splash screen as a full screen activity.
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

        val bundle: Bundle = intent.extras!!
        vcode = bundle.get("vcode") as String
        email = bundle.get("email") as String
        token = bundle.get("token") as String

        localStorage = LocalStorage(this@VerifyOTPActivity)

        pinView = findViewById(R.id.pin_view)
        resendCode = findViewById(R.id.resend_code_text)
        btn_verify = findViewById(R.id.btn_verify)

        pinView.requestFocus()
        val inputMethodManager: InputMethodManager =
            getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.toggleSoftInput(
            InputMethodManager.SHOW_FORCED,
            InputMethodManager.HIDE_IMPLICIT_ONLY
        )

        pinView.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun afterTextChanged(p0: Editable?) {
                if (p0.toString().trim() == vcode) {
                    user_entered_code = p0.toString().trim()
                    showProgressDialog(resources.getString(R.string.please_wait))
                    verifyOtp(p0.toString())
                } else{
                    user_entered_code = ""
                    showErrorSnackBar("Code Does Not Matched!!")
                }
            }

        })

        resendCode.setOnClickListener {
            showProgressDialog(resources.getString(R.string.please_wait))
            val retrofit = ServiceBuilder.buildService(APIListPlixJson::class.java)
            retrofit.requestResendOtp(email)
                .enqueue(
                    object:Callback<Int>{
                        override fun onResponse(call: Call<Int>, response: Response<Int>) {
                            if(response.isSuccessful)
                            {
                                vcode = response.body().toString()
                                Toast.makeText(this@VerifyOTPActivity,"Code Resend Successful",Toast.LENGTH_LONG).show()
                            } else {
                                showErrorSnackBar(response.errorBody().toString())
                            }
                            hideProgressDialog()
                        }

                        override fun onFailure(call: Call<Int>, t: Throwable) {
                            showErrorSnackBar(t.message.toString())
                            hideProgressDialog()
                        }

                    }
                )
        }
        btn_verify.setOnClickListener {
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

    fun verifyOtp(toString: String) {
        val retrofit = ServiceBuilder.buildService(APIListPlixJson::class.java)
        val obj = RequestOtpModel(email, vcode)
        retrofit.requestVerifyOtp(obj).enqueue(
            object : Callback<ResponseOtp> {
                override fun onResponse(
                    call: Call<ResponseOtp>,
                    response: Response<ResponseOtp>
                ) {
                    if (response.isSuccessful) {
                        localStorage.token = token
                        localStorage.email = email
                        Toast.makeText(
                            this@VerifyOTPActivity,
                            "Verification Successful",
                            Toast.LENGTH_LONG
                        ).show()
                        startActivity(Intent(this@VerifyOTPActivity, MainActivity::class.java))
                        finish()
                    } else {
                        val jObjError = JSONObject(
                            response.errorBody()!!.string()
                        )
                        val error = jObjError.getString("error")
                        showErrorSnackBar(error)
                    }

                    hideProgressDialog()
                }

                override fun onFailure(call: Call<ResponseOtp>, t: Throwable) {
                }

            }
        )

    }
}