package com.softwarealliance.listplix.model.requests

data class RequestOtpModel(
    val email: String,
    val verify_code: String
)
