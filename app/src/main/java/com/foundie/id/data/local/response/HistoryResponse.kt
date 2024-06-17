package com.foundie.id.data.local.response

import kotlinx.parcelize.Parcelize
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

data class HistoryResponse(

	@field:SerializedName("data")
	val data: List<PredictDataItem>,

	@field:SerializedName("error")
	val error: Boolean,

	@field:SerializedName("message")
	val message: String,
)

@Parcelize
data class PredictDataItem(

	@field:SerializedName("prediction")
	val prediction: String,

	@field:SerializedName("message")
	val message: String,

	@field:SerializedName("type")
	val type: String,

	@field:SerializedName("email")
	val email: String
) : Parcelable
