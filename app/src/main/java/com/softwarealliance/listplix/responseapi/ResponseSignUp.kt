package com.softwarealliance.listplix.responseapi

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ResponseSignUp(
    @SerializedName("token")
    @Expose
    val token: String,
    @SerializedName("code")
    @Expose
    val code: String
)