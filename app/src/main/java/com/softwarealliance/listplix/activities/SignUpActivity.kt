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
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatEditText
import androidx.appcompat.widget.Toolbar
import com.google.android.material.textfield.TextInputLayout
import com.softwarealliance.listplix.R
import com.softwarealliance.listplix.`interface`.APIListPlixJson
import com.softwarealliance.listplix.model.RequestSignUpModel
import com.softwarealliance.listplix.responseapi.ResponseSignUp
import com.softwarealliance.listplix.service.ServiceBuilder
import com.softwarealliance.listplix.utils.Constants.PASSWORD_PATTERN
import com.softwarealliance.listplix.utils.Constants.isValidPattern
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SignUpActivity : BaseActivity() {
    private var isSelectedRole = false
    private var isSelectedDepartment = false

    private lateinit var btnSignUp: AppCompatButton
    private lateinit var et_name: AppCompatEditText
    private lateinit var et_email: AppCompatEditText
    private lateinit var et_password: AppCompatEditText
    private lateinit var et_cnf_password: AppCompatEditText
    private lateinit var ac_department: AutoCompleteTextView
    private lateinit var ac_role: AutoCompleteTextView

    private lateinit var til_name: TextInputLayout
    private lateinit var til_email: TextInputLayout
    private lateinit var til_password: TextInputLayout
    private lateinit var til_cnf_password: TextInputLayout
    private lateinit var til_role: TextInputLayout
    private lateinit var til_department: TextInputLayout

    private lateinit var str_role: String
    private lateinit var str_department: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

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

        et_name.addTextChangedListener(TextFieldValidation(et_name))
        et_email.addTextChangedListener(TextFieldValidation(et_email))
        et_password.addTextChangedListener(TextFieldValidation(et_password))
        et_cnf_password.addTextChangedListener(TextFieldValidation(et_cnf_password))


        // set color of TIL
        til_name.setBoxStrokeColorStateList(setStrokeColorState())
        til_email.setBoxStrokeColorStateList(setStrokeColorState())
        til_password.setBoxStrokeColorStateList(setStrokeColorState())
        til_cnf_password.setBoxStrokeColorStateList(setStrokeColorState())
        til_role.setBoxStrokeColorStateList(setStrokeColorState())
        til_department.setBoxStrokeColorStateList(setStrokeColorState())


        btnSignUp.setOnClickListener {
            registerUser()
        }
    }

    private fun initView() {
        btnSignUp = findViewById(R.id.btn_sign_up)
        et_name = findViewById(R.id.et_name)
        et_email = findViewById(R.id.et_email)
        et_password = findViewById(R.id.et_password)
        et_cnf_password = findViewById(R.id.et_cnf_password)
        ac_department = findViewById(R.id.ac_department)
        ac_role = findViewById(R.id.ac_role)

        til_name = findViewById(R.id.til_name)
        til_email = findViewById(R.id.til_email)
        til_password = findViewById(R.id.til_password)
        til_cnf_password = findViewById(R.id.til_cnf_password)
        til_role = findViewById(R.id.til_role)
        til_department = findViewById(R.id.til_department)

        val adapterDepartment = ArrayAdapter.createFromResource(
            this,
            R.array.department,
            R.layout.dropdown_menu_popup_item
        )

        ac_department.setAdapter(adapterDepartment)

        val adapterRole = ArrayAdapter.createFromResource(
            this,
            R.array.role,
            R.layout.dropdown_menu_popup_item
        )

        ac_role.setAdapter(adapterRole)

        ac_department.setOnItemClickListener { adapterView, view, i, l ->
            str_department = adapterView.getItemAtPosition(i).toString()
            isSelectedDepartment = true
        }

        ac_role.setOnItemClickListener { adapterView, view, i, l ->
            str_role = adapterView.getItemAtPosition(i).toString()
            isSelectedRole = true
        }

    }

    /**
     * A function to register a user to our app using the API
     */
    private fun registerUser() {

        if (isValidate()) {
            sendRegister()
        }
    }

    private fun sendRegister() {
        // Show the progress dialog.
        showProgressDialog(resources.getString(R.string.please_wait))
        // Here we get the text from editText and trim the space
        val str_name: String = et_name.text.toString().trim { it <= ' ' }
        val str_email: String = et_email.text.toString().trim { it <= ' ' }
        val str_password: String = et_password.text.toString().trim { it <= ' ' }

        val retrofit = ServiceBuilder.buildService(APIListPlixJson::class.java)
        val obj = RequestSignUpModel(str_name,str_email,str_password,str_role,str_department)
        retrofit.requestRegister(obj).enqueue(
            object : Callback<ResponseSignUp>{
                override fun onResponse(
                    call: Call<ResponseSignUp>,
                    response: Response<ResponseSignUp>
                ) {
                    Log.d("SignUpResponseYahoo", "onResponse: Body: ${response.body()} \n Message: ${response.message()} \n ${response.code()}")
                    when (val code = response.code()) {
                        201, 200 ->{
//                            alertSuccess("Register Successfully.")
                            val vcode = response.body()?.code?:null
                            val token = response.body()?.token?:null
                            Log.d("ResponseDataGetting", "onResponse: Code-> $vcode\nToken -> $token")
                            userRegisteredSuccess(str_email,vcode,token)
                        }
                        422 -> alertFail(response.message().toString())
                        401 -> {
                            hideProgressDialog()
                            val jObjError = JSONObject(
                                response.errorBody()!!.string()
                            )
                            val error = jObjError.getJSONObject("error").getString("email")
                            if(error.equals(getString(R.string.error_email)))
                            {
                                til_email.setError("The Email has already been taken")
                                et_email.requestFocus()
                            }
//                            alertFail(response.body()!!.error.email.toString())
                        }
                        else ->{
                            hideProgressDialog()
                            Toast.makeText(this@SignUpActivity, "Error $code", Toast.LENGTH_LONG)
                                .show()
                        }
                    }
                }

                override fun onFailure(call: Call<ResponseSignUp>, t: Throwable) {
                    Log.d("SignUpFailureResponse", "onFailure: message: ${t.message} \n PrintStackTrace: ${t.printStackTrace()} \ncause ${t.cause}")
                }

            }
        )

/*
        var params = JSONObject()
        try {
            params.put("name", str_name)
            params.put("email", str_email)
            params.put("password", str_password)
            params.put("role", str_role)
            params.put("department", str_department)
        } catch (e: JSONException) {
            e.printStackTrace();
        }

        var data = params.toString()
        var url = getString(R.string.api_server) + "/register"

        Thread {
            val http = Http(this@SignUpActivity, url)
            http.setMethod("POST")
            http.setData(data)
            http.send()

            runOnUiThread {
                when (val code = http.statusCode) {
                    201, 200 -> {
                        // here we get token where we get code
                        alertSuccess("Register Successfully.")
                    }
                    422 -> {
                        try {
                            val response = JSONObject(http.response)
                            val msg = response.getString("error")
                            alertFail(msg)
                        } catch (e: JSONException) {
                            e.printStackTrace()
                        }
                    }
                    else -> {
                        Toast.makeText(this@SignUpActivity, "Error $code", Toast.LENGTH_LONG)
                            .show()
                    }
                }
            }
        }.start()
*/

    }

    fun userRegisteredSuccess(str_email: String, vcode: String?, token: String?) {

        Toast.makeText(
            this@SignUpActivity,
            "You have successfully registered.",
            Toast.LENGTH_SHORT
        ).show()

        // Hide the progress dialog
        hideProgressDialog()

        val intent = Intent(this@SignUpActivity,VerifyOTPActivity::class.java)
        intent.putExtra("email",str_email)
        intent.putExtra("vcode",vcode)
        intent.putExtra("token",token)
        startActivity(intent)
        finish()

    }


    fun alertFail(s:String){
        // Hide the progress dialog
        hideProgressDialog()

        AlertDialog.Builder(this)
            .setTitle("Failed")
            .setIcon(android.R.drawable.ic_dialog_alert)
            .setMessage(s)
            .setPositiveButton("Login",DialogInterface.OnClickListener(){ dialogInterface: DialogInterface, i: Int ->
                onBackPressed()
            })
            .show()
    }

    /**
     * A function for actionBar Setup.
     */
    private fun setupActionBar() {

        val toolbar_sign_up_activity: Toolbar = findViewById(R.id.toolbar_sign_up_activity)

        setSupportActionBar(toolbar_sign_up_activity)

        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_black_color_back_24dp)
        }

        toolbar_sign_up_activity.setNavigationOnClickListener { onBackPressed() }
    }

    /**
     * A function for Validation Setup.
     */
    private fun isValidate(): Boolean {
        return (validateFullName() && validateGmail() && validatePassword() && validateConfirmPassword() && validateDepartment() && validateRole())
    }

    fun validateFullName(): Boolean {
        if (et_name.getText().toString().isEmpty()) {
            til_name.setError("Required Field!")
            et_name.requestFocus()
            return false
        } else if (et_name.getText().toString().length < 3) {
            til_name.setError("Please Enter Full Name")
            et_name.requestFocus()
            return false
        } else if( et_name.getText().toString().length > 50){
            til_name.setError("Entered name is too long!")
            et_name.requestFocus()
            return false
        }else {
            til_name.setErrorEnabled(false)
        }
        return true
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
        } else if (et_password.getText().toString().length < 8) {
            til_password.setError("Password must more then 8")
            et_password.requestFocus()
            return false
        } else if (!isValidPattern(
                et_password.getText().toString(),
                PASSWORD_PATTERN
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

    private fun validateRole(): Boolean {
        if (isSelectedRole) {
            return isSelectedRole
        }
        Toast.makeText(this, "Select Role before proceed", Toast.LENGTH_LONG).show()
        return isSelectedRole
    }

    private fun validateDepartment(): Boolean {
        if (isSelectedDepartment) {
            return isSelectedDepartment
        }
        Toast.makeText(this, "Select Department before proceed", Toast.LENGTH_LONG).show()
        return isSelectedDepartment
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
                R.id.et_name -> {
                    validateFullName()
                }
                R.id.et_email -> {
                    validateGmail()
                }
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