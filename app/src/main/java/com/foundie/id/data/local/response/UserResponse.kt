package com.foundie.id.data.local.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

data class UserResponse(
    @field:SerializedName("message")
    val message: String,

    @field:SerializedName("user")
    val user: List<User>,

    @field:SerializedName("error")
    val error: Boolean
)

@Parcelize
data class User(
    @field:SerializedName("role")
    val role: String,

    @field:SerializedName("email")
    val email: String,

    @field:SerializedName("gender")
    val gender: String,

    @field:SerializedName("phone")
    val phone: String,

    @field:SerializedName("location")
    val location: String,

    @field:SerializedName("profileImageUrl")
    val profileImageUrl: String,

    @field:SerializedName("name")
    val name: String,

    @field:SerializedName("coverImageUrl")
    val coverImageUrl: String
) : Parcelable