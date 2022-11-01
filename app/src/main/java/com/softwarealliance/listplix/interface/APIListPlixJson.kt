package com.softwarealliance.listplix.`interface`

import com.softwarealliance.listplix.model.RequestOtpModel
import com.softwarealliance.listplix.model.RequestSignInModel
import com.softwarealliance.listplix.model.RequestSignUpModel
import com.softwarealliance.listplix.responseapi.*
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface APIListPlixJson {
    @POST("register")
    fun requestRegister(
        @Body requestModel: RequestSignUpModel
    ): Call<ResponseSignUp>


    @POST("login")
    fun requestLogin(
        @Body requestModel: RequestSignInModel
    ): Call<ResponseSignIn>

    @POST("verify")
    fun requestVerifyOtp(
        @Body requestOtpModel: RequestOtpModel
    ): Call<ResponseOtp>

    @FormUrlEncoded
    @POST("resend")
    fun requestResendOtp(
        @Field("email") email:String
    ):Call<Int>

    @FormUrlEncoded
    @POST("forget_password")
    fun requestResetPassword(
        @Field("email") email:String
    ):Call<ResponseForgetPassword>

    @POST("update_password")
    fun requestUpdatePassword(
        @Body requestModel: RequestSignInModel
    ):Call<ResponseUpdatePassword>
}