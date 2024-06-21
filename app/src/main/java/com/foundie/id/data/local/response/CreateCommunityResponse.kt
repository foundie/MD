package com.foundie.id.data.local.response

import kotlinx.parcelize.Parcelize
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

@Parcelize
data class CreateCommunityResponse(

	@field:SerializedName("message")
	val message: String,

	@field:SerializedName("error")
	val error: Boolean

) : Parcelable