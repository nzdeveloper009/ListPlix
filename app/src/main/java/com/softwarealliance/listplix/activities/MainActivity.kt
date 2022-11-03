package com.softwarealliance.listplix.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import com.softwarealliance.listplix.R
import com.softwarealliance.listplix.utils.LocalStorage

class MainActivity : BaseActivity() {
    private lateinit var localStorage: LocalStorage
    private lateinit var tv_data_user: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        localStorage = LocalStorage(this@MainActivity)
        tv_data_user = findViewById(R.id.tv_data_user)


       /* val retrofit = ServiceBuilder.buildService(APIListPlixJson::class.java)
        retrofit.requestGetUserDetail(localStorage.token).enqueue(
            object:Callback<ResponseUserDetail>{
                override fun onResponse(
                    call: Call<ResponseUserDetail>,
                    response: Response<ResponseUserDetail>
                ) {
                    if(response.isSuccessful)
                    {
                        val str_res = "Name: ${response.body()?.user?.name}\nEmail: ${response.body()?.user?.email}\nDepartment: ${response.body()?.user?.email}\n" +
                                "Role: ${response.body()?.user?.role}"
                        tv_data_user.text = str_res
                    }else {
                        showErrorSnackBar(response.errorBody().toString())
                    }
                }

                override fun onFailure(call: Call<ResponseUserDetail>, t: Throwable) {
                    showErrorSnackBar(t.message.toString())

                }

            }
        )*/

    }

    fun LogOut(view: View) {
        localStorage.loggedIn = false
        localStorage.token = ""
        localStorage.email = ""

        startActivity(Intent(this, IntroActivity::class.java))
        finish()
    }
}