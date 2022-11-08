package com.softwarealliance.listplix.model.responseapi

import android.os.Parcel
import android.os.Parcelable


data class User(
    val created_at: String = "",
    val department: String = "",
    val email: String = "",
    val email_verified_at: String = "",
    val id: Int = 0,
    val name: String = "",
    val role: String = "",
    val updated_at: String = "",
    val verify_code: String = "",
    val mobile: Long = 0,
    val fcmToken:String = ""
): Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readInt(),
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readLong(),
        parcel.readString()!!
    ) {
    }

    override fun describeContents() = 0
    override fun writeToParcel(dest: Parcel, p1: Int) = with(dest){
        this.writeString(created_at)
        this.writeString(department)
        this.writeString(email)
        this.writeString(email_verified_at)
        this.writeString(id.toString())
        this.writeString(name)
        this.writeString(role)
        this.writeString(updated_at)
        this.writeString(verify_code)
        this.writeString(mobile.toString())
        this.writeString(fcmToken)
    }

    companion object CREATOR : Parcelable.Creator<User> {
        override fun createFromParcel(parcel: Parcel): User {
            return User(parcel)
        }

        override fun newArray(size: Int): Array<User?> {
            return arrayOfNulls(size)
        }
    }
}