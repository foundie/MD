package com.foundie.id.data.local.response

import kotlinx.parcelize.Parcelize
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

@Parcelize
data class GroupCommunityResponse(

	@field:SerializedName("data")
	val data: List<GroupsDataItem>,

	@field:SerializedName("message")
	val message: String,

	@field:SerializedName("error")
	val error: Boolean

) : Parcelable

@Parcelize
data class GroupsDataItem(

	@field:SerializedName("creator")
	val creator: String,

	@field:SerializedName("coverImageUrl")
	val coverImageUrl: String,

	@field:SerializedName("topics")
	val topics: List<String>,

	@field:SerializedName("description")
	val description: String,

	@field:SerializedName("id")
	val id: String,

	@field:SerializedName("subscription")
	val subscription: Int,

	@field:SerializedName("title")
	val title: String,

	@field:SerializedName("profileImageUrl")
	val profileImageUrl: String,

	@field:SerializedName("timestamp")
	val timestamp: String
) : Parcelable
