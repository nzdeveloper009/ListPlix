package com.softwarealliance.listplix.model.responseapi

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Message(
    val created_at: String,
    val id: Int,
    val project_description: String,
    val project_title: String,
    val updated_at: String
): Parcelable