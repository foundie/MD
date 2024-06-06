package com.foundie.id.data.local.response

import com.google.gson.annotations.SerializedName

data class LoginGoogleResponse(

    @field:SerializedName("user")
    val loginGoogleResult: UserLoginResponse,

    @field:SerializedName("error")
    val error: Boolean,

    @field:SerializedName("message")
    val message: String,

    @field:SerializedName("setPassword")
    val setPassword: Boolean
)