package com.foundie.id.data.local.response

import com.google.gson.annotations.SerializedName

data class AddPasswordResponse(

    @field:SerializedName("error")
    val error: Boolean,

    @field:SerializedName("message")
    val message: String,

)