package com.foundie.id.data.local.response

import android.gesture.Prediction
import com.google.gson.annotations.SerializedName

data class MakeUpStyleResponse(

    @field:SerializedName("prediction")
    val prediction: Prediction,

    @field:SerializedName("message")
    val message: String,
)

