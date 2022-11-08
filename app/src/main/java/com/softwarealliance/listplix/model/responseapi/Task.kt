package com.softwarealliance.listplix.model.responseapi


data class Task(
    val created_at: String,
    val description: String,
    val id: Int,
    val project_id: String,
    val status: String,
    val title: String,
    val updated_at: String,
    val user_id: String
)