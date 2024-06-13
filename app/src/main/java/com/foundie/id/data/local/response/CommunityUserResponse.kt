package com.foundie.id.data.local.response

import kotlinx.parcelize.Parcelize
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

@Parcelize
data class CommunityUserResponse(

	@field:SerializedName("data")
	val data: List<DataPostItem>,

	@field:SerializedName("message")
	val message: String,

	@field:SerializedName("error")
	val error: Boolean,

	@field:SerializedName("status")
	val status: Int
) : Parcelable

@Parcelize
data class Timestamp(

	@field:SerializedName("_nanoseconds")
	val nanoseconds: Int,

	@field:SerializedName("_seconds")
	val seconds: Int
) : Parcelable

@Parcelize
data class DataPostItem(

	@field:SerializedName("likesCount")
	val likesCount: Int,

	@field:SerializedName("createdTimestamp")
	val createdTimestamp: Long,

	@field:SerializedName("imageUrls")
	val imageUrls: List<String>,

	@field:SerializedName("postId")
	val postId: String,

	@field:SerializedName("text")
	val text: String,

	@field:SerializedName("title")
	val title: String,

	@field:SerializedName("email")
	val email: String,

	@field:SerializedName("timestamp")
	val timestamp: Timestamp,

	@field:SerializedName("profileImageUrl")
	val profileImageUrl: String,

	@field:SerializedName("groupPost")
	val groupPost: Boolean,

	@field:SerializedName("groupId")
	val groupId: String
) : Parcelable
