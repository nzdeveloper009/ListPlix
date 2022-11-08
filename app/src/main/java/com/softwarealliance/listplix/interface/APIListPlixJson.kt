package com.softwarealliance.listplix.`interface`

import com.softwarealliance.listplix.model.requests.RequestOtpModel
import com.softwarealliance.listplix.model.requests.RequestSignInModel
import com.softwarealliance.listplix.model.requests.RequestSignUpModel
import com.softwarealliance.listplix.model.responseapi.*
import com.softwarealliance.listplix.utils.Constants.FORGET_PASSWORD
import com.softwarealliance.listplix.utils.Constants.GET_TASK_BY_ID
import com.softwarealliance.listplix.utils.Constants.GET_USER_INFO
import com.softwarealliance.listplix.utils.Constants.LOGIN
import com.softwarealliance.listplix.utils.Constants.PROJECT_BY_ID
import com.softwarealliance.listplix.utils.Constants.REGISTER
import com.softwarealliance.listplix.utils.Constants.RESEND_OTP
import com.softwarealliance.listplix.utils.Constants.UPDATE_PASSWORD
import com.softwarealliance.listplix.utils.Constants.VERIFY
import retrofit2.Call
import retrofit2.http.*

interface APIListPlixJson {
    @POST(REGISTER)
    fun requestRegister(
        @Body requestModel: RequestSignUpModel
    ): Call<ResponseSignUp>


    @POST(LOGIN)
    fun requestLogin(
        @Body requestModel: RequestSignInModel
    ): Call<ResponseSignIn>

    @POST(VERIFY)
    fun requestVerifyOtp(
        @Body requestOtpModel: RequestOtpModel
    ): Call<ResponseOtp>

    @FormUrlEncoded
    @POST(RESEND_OTP)
    fun requestResendOtp(
        @Field("email") email:String
    ):Call<Int>

    @FormUrlEncoded
    @POST(FORGET_PASSWORD)
    fun requestResetPassword(
        @Field("email") email:String
    ):Call<ResponseForgetPassword>

    @POST(UPDATE_PASSWORD)
    fun requestUpdatePassword(
        @Body requestModel: RequestSignInModel
    ):Call<ResponseUpdatePassword>


    @GET(GET_USER_INFO)
    fun requestGetUserDetail(): Call<ResponseUserDetail>

    @FormUrlEncoded
    @POST(PROJECT_BY_ID)
    fun requestForUserBoard(
        @Field("user_id") userid:Int
    ) : Call<ResponseGetProjectByID>


    @FormUrlEncoded
    @POST(GET_TASK_BY_ID)
    fun requestForUserTasks(
        @Field("user_id") userid:Int,
        @Field("project_id") projectID:Int
    ) : Call<ResponseGetTaskByID>


}