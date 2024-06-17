package com.foundie.id.data.local.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

data class PredictResponse(

    @field:SerializedName("data")
    val data: MakeUpStyleResponse,

    @field:SerializedName("error")
    val error: Boolean
)

@Parcelize
data class MakeUpStyleResponse(

    @field:SerializedName("predicted_class")
    val prediction: String,

    @field:SerializedName("message")
    val message: String,
) : Parcelable
