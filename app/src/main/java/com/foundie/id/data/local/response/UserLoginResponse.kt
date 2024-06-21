package com.foundie.id.data.local.response

import com.google.gson.annotations.SerializedName

data class UserLoginResponse(

    @field:SerializedName("name")
    val name: String,

    @field:SerializedName("email")
    val email: String,

    @field:SerializedName("role")
    val role: String,

    @field:SerializedName("token")
    val token: String
)

