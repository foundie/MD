package com.foundie.id.data.local.response

import com.google.gson.annotations.SerializedName

data class EditProfileResponse(

    @field:SerializedName("error")
    val error: Boolean,

    @field:SerializedName("message")
    val message: String
)