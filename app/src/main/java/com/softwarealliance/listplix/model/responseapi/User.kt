package com.softwarealliance.listplix.model.responseapi

data class User(
    val created_at: String = "",
    val department: String = "",
    val email: String = "",
    val email_verified_at: String = "",
    val id: Int,
    val name: String = "",
    val role: String = "",
    val updated_at: String = "",
    val verify_code: String = ""
)