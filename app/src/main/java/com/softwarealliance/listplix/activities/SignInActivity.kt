package com.softwarealliance.listplix.activities

import android.content.DialogInterface
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.util.Patterns
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatEditText
import androidx.appcompat.widget.Toolbar
import com.google.android.material.textfield.TextInputLayout
import com.softwarealliance.listplix.R
import com.softwarealliance.listplix.activities.forgetpassword.ForgetPasswordActivity01
import com.softwarealliance.listplix.api.ApiClient
import com.softwarealliance.listplix.model.requests.RequestSignInModel
import com.softwarealliance.listplix.model.responseapi.ResponseSignIn
import com.softwarealliance.listplix.model.responseapi.User
import com.softwarealliance.listplix.utils.Constants.loadUserData
import com.softwarealliance.listplix.utils.LocalStorage
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class SignInActivity : BaseActivity() {

    private lateinit var btn_sign_in: AppCompatButton
    private lateinit var et_email: AppCompatEditText
    private lateinit var et_password: AppCompatEditText
    private lateinit var til_email: TextInputLayout
    private lateinit var til_password: TextInputLayout

    private lateinit var localStorage: LocalStorage

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)

        // This is used to hide the status bar and make the splash screen as a full screen activity.
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        localStorage = LocalStorage(this@SignInActivity)
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
        til_email.setBoxStrokeColorStateList(setStrokeColorState())
        til_password.setBoxStrokeColorStateList(setStrokeColorState())

        et_email.addTextChangedListener(TextFieldValidation(et_email))
        et_password.addTextChangedListener(TextFieldValidation(et_password))

        btn_sign_in.setOnClickListener {

            if (isValidate()) {
                signInRegisteredUser()
            }
        }

    }

    private fun signInRegisteredUser() {
        // Show the progress dialog.
        showProgressDialog(resources.getString(R.string.please_wait))
        val str_email: String = et_email.text.toString().trim { it <= ' ' }
        val str_password: String = et_password.text.toString().trim { it <= ' ' }

        val apiClient = ApiClient()
        val obj = RequestSignInModel(str_email, str_password)
        apiClient.getApiService(this).requestLogin(obj).enqueue(
            object : Callback<ResponseSignIn> {
                override fun onResponse(
                    call: Call<ResponseSignIn>,
                    response: Response<ResponseSignIn>
                ) {
                    when (val code = response.code()) {
                        201, 200 -> {
                            val token = response.body()!!.token
                            localStorage.token = token
                            localStorage.email = str_email
                            localStorage.loggedIn = true
                            Log.d("Tokeeeeeen", "onResponse: Token-> $token")
                            loadUserData(this@SignInActivity)
                        }
                        422 -> {
                            hideProgressDialog()
                            val jObjError = JSONObject(
                                response.errorBody()!!.string()
                            )
                            val error = jObjError.getString("error")
                            alertFail(error)
                        }
                        // provided credentials don not match our records
                        401 -> {
                            hideProgressDialog()
//                            til_email.setError(response.errorBody().toString())
                            val jObjError = JSONObject(
                                response.errorBody()!!.string()
                            )
                            val error = jObjError.getString("error")
                            if(error.equals("Email is not verified") || error.equals("Email is not registered")){
                                til_email.setError(error)
                                et_email.requestFocus()
                            } else {
                                til_password.setError(error)
                                et_password.requestFocus()
                            }

//                            response.body()?.let { alertFail(it.error) }
                        }
                        else -> {
                            hideProgressDialog()
                            Toast.makeText(
                                this@SignInActivity,
                                "Error $code",
                                Toast.LENGTH_LONG
                            )
                                .show()
                        }
                    }
                }

                override fun onFailure(call: Call<ResponseSignIn>, t: Throwable) {
                    Log.d(
                        "SignInFailureResponse",
                        "onFailure: message: ${t.message} \n PrintStackTrace: ${t.printStackTrace()} \ncause ${t.cause}"
                    )
                }

            }
        )
    }


    fun signInSuccess(user:User) {

        // Hide the progress dialog
        hideProgressDialog()

        Toast.makeText(
            this@SignInActivity,
            "You have successfully Login.",
            Toast.LENGTH_SHORT
        ).show()

        startActivity(Intent(this@SignInActivity, MainActivity::class.java))
        this.finish()

    }




    fun alertFail(s: String) {
        // Hide the progress dialog
        hideProgressDialog()

        AlertDialog.Builder(this)
            .setTitle("Failed")
            .setIcon(android.R.drawable.ic_dialog_alert)
            .setMessage(s)
            .setPositiveButton("Login",
                DialogInterface.OnClickListener() { dialogInterface: DialogInterface, i: Int ->
                    onBackPressed()
                })
            .show()
    }


    private fun initView() {

        et_email = findViewById(R.id.et_email)
        et_password = findViewById(R.id.et_password)
        btn_sign_in = findViewById(R.id.btn_sign_in)

        til_email = findViewById(R.id.til_email)
        til_password = findViewById(R.id.til_password)
    }

    private fun setupActionBar() {

        val toolbar_sign_in_activity: Toolbar = findViewById(R.id.toolbar_sign_in_activity)

        setSupportActionBar(toolbar_sign_in_activity)

        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_black_color_back_24dp)
        }

        toolbar_sign_in_activity.setNavigationOnClickListener { onBackPressed() }
    }

    private fun isValidate(): Boolean {
        return (validateGmail() && validatePassword())
    }

    fun validateGmail(): Boolean {
        if (et_email.getText().toString().isEmpty()) {
            til_email.setError("Required Field!")
            et_email.requestFocus()
            return false
        } else if (!Patterns.EMAIL_ADDRESS.matcher(et_email.getText().toString()).matches()) {
            til_email.setError("Email is not valid")
            et_email.requestFocus()
            return false
        } else {
            til_email.setErrorEnabled(false)
        }
        return true
    }

    fun validatePassword(): Boolean {
        if (et_password.getText().toString().isEmpty()) {
            til_password.setError("Required Field!")
            et_password.requestFocus()
            return false
        } else {
            til_password.setErrorEnabled(false)
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
                R.id.et_email -> validateGmail()
                R.id.et_password -> validatePassword()
            }
        }

        override fun afterTextChanged(p0: Editable?) {
        }

    }

    fun forgetPassword(view: View) {
        startActivity(Intent(this@SignInActivity,ForgetPasswordActivity01::class.java))
    }

}