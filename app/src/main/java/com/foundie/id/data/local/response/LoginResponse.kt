package com.foundie.id.data.local.response

import com.google.gson.annotations.SerializedName

data class LoginResponse(

    @field:SerializedName("user")
    val loginResult: UserLoginResponse,

    @field:SerializedName("message")
    val message: String
)