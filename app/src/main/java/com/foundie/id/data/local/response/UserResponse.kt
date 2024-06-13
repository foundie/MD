package com.foundie.id.data.local.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

data class UserResponse(
    @field:SerializedName("message")
    val message: String,

    @field:SerializedName("user")
    val user: User,

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

    @field:SerializedName("profilePictureUrl")
    val profileImageUrl: String,

    @field:SerializedName("name")
    val name: String,

    @field:SerializedName("description")
    val description: String,

    @field:SerializedName("coverPictureUrl")
    val coverImageUrl: String
) : Parcelable