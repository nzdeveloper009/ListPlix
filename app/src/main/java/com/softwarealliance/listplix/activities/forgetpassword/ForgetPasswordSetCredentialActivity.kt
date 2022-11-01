package com.softwarealliance.listplix.activities.forgetpassword

import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.WindowManager
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatEditText
import com.google.android.material.textfield.TextInputLayout
import com.softwarealliance.listplix.R
import com.softwarealliance.listplix.`interface`.APIListPlixJson
import com.softwarealliance.listplix.activities.BaseActivity
import com.softwarealliance.listplix.model.RequestSignInModel
import com.softwarealliance.listplix.responseapi.ResponseUpdatePassword
import com.softwarealliance.listplix.service.ServiceBuilder
import com.softwarealliance.listplix.utils.Constants
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ForgetPasswordSetCredentialActivity : BaseActivity() {

    lateinit var btn_update:AppCompatButton
    private lateinit var et_password: AppCompatEditText
    private lateinit var et_cnf_password: AppCompatEditText
    private lateinit var til_password: TextInputLayout
    private lateinit var til_cnf_password: TextInputLayout
    lateinit var email: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forget_password_set_credential)

        // This is used to hide the status bar and make the splash screen as a full screen activity.
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        val bundle: Bundle = intent.extras!!
        email = bundle.get("email") as String


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
        et_password.addTextChangedListener(TextFieldValidation(et_password))
        et_cnf_password.addTextChangedListener(TextFieldValidation(et_cnf_password))

        btn_update.setOnClickListener {
            if(isValidate())
                resetPassword()
        }
    }

    private fun resetPassword() {
        // Show the progress dialog.
        showProgressDialog(resources.getString(R.string.please_wait))
        // Here we get the text from editText and trim the space
        val str_password: String = et_password.text.toString().trim { it <= ' ' }
        val retrofit = ServiceBuilder.buildService(APIListPlixJson::class.java)
        val obj = RequestSignInModel(email,str_password)
        retrofit.requestUpdatePassword(obj).enqueue(
            object:Callback<ResponseUpdatePassword>{
                override fun onResponse(call: Call<ResponseUpdatePassword>, response: Response<ResponseUpdatePassword>) {
                    if(response.isSuccessful)
                    {
                        startActivity(Intent(this@ForgetPasswordSetCredentialActivity,UpdatePasswordSuccessfulActivity::class.java))
                        finish()
                    } else {
                        showErrorSnackBar(response.errorBody().toString())
                    }
                    hideProgressDialog()
                }

                override fun onFailure(call: Call<ResponseUpdatePassword>, t: Throwable) {
                    hideProgressDialog()
                    showErrorSnackBar(t.message.toString())
                }

            }
        )

    }

    private fun initView() {

        et_password = findViewById(R.id.et_password)
        et_cnf_password = findViewById(R.id.et_cnf_password)

        til_password = findViewById(R.id.til_password)
        til_cnf_password = findViewById(R.id.til_cnf_password)

        btn_update = findViewById(R.id.btn_update)


        til_password.setBoxStrokeColorStateList(setStrokeColorState())
        til_cnf_password.setBoxStrokeColorStateList(setStrokeColorState())

    }

    /**
     * A function for Validation Setup.
     */
    private fun isValidate(): Boolean {
        return ( validatePassword() && validateConfirmPassword())
    }

    fun validatePassword(): Boolean {
        if (et_password.getText().toString().isEmpty()) {
            til_password.setError("Required Field!")
            et_password.requestFocus()
            return false
        } else if (et_password.getText().toString().length < 8) {
            til_password.setError("Password must more then 8")
            et_password.requestFocus()
            return false
        } else if (!Constants.isValidPattern(
                et_password.getText().toString(),
                Constants.PASSWORD_PATTERN
            )
        ) {
            til_password.setError("Please enter strong password including capital, lower case letter, special symbol and number")
            et_password.requestFocus()
            return false
        } else {
            til_password.setErrorEnabled(false)
        }
        return true
    }

    fun validateConfirmPassword(): Boolean {
        if (et_cnf_password.text.toString().isEmpty()) {
            til_cnf_password.setError("Required Field!")
            et_cnf_password.requestFocus()
            return false
        } else if (et_cnf_password.text.toString()?.equals(et_password.text.toString()) == false) {
            til_cnf_password.setError("Password not Matched")
            et_cnf_password.requestFocus()
            return false
        } else {
            til_cnf_password.setErrorEnabled(false)
        }
        return true
    }

    inner class TextFieldValidation : TextWatcher {

        lateinit var v: View

        constructor(v: View) {
            this.v = v
        }

        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        }
        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            when (v.id) {
                R.id.et_password -> {
                    validatePassword()
                }
                R.id.et_cnf_password -> {
                    validateConfirmPassword()
                }
            }
        }
        override fun afterTextChanged(p0: Editable?) {
        }

    }

}