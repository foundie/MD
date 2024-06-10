package com.foundie.id.data.local.response

import com.google.gson.annotations.SerializedName

data class PredictResponse(

    @field:SerializedName("data")
    val data: MakeUpStyleResponse,

    @field:SerializedName("error")
    val error: Boolean
)