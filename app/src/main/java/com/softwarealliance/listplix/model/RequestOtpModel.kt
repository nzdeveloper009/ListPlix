package com.softwarealliance.listplix.model

data class RequestOtpModel(
    val email: String,
    val verify_code: String
)
