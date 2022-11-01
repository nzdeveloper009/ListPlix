package com.softwarealliance.listplix.model

data class RequestSignUpModel(
    val name:String,
    val email:String,
    val password:String,
    val role:String,
    val department:String
)
