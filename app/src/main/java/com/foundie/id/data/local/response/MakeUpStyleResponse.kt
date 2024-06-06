package com.foundie.id.data.local.response

import com.google.gson.annotations.SerializedName

data class MakeUpStyleResponse(

    @field:SerializedName("message")
    val message: String,

    @field:SerializedName("error")
    val error: Boolean
)