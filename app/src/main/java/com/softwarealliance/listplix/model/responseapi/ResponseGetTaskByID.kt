package com.softwarealliance.listplix.model.responseapi

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ResponseGetTaskByID(
    @SerializedName("task")
    @Expose
    val task: List<Task>
)