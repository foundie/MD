package com.foundie.id.data.local.response

import kotlinx.parcelize.Parcelize
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

@Parcelize
data class UserDetailResponse(

	@field:SerializedName("data")
	val data: Data,

	@field:SerializedName("message")
	val message: String,

	@field:SerializedName("error")
	val error: Boolean,

	@field:SerializedName("status")
	val status: Int
) : Parcelable

@Parcelize
data class UserDetail(

	@field:SerializedName("profilePictureUrl")
	val profilePictureUrl: String,

	@field:SerializedName("gender")
	val gender: String,

	@field:SerializedName("phone")
	val phone: String,

	@field:SerializedName("name")
	val name: String,

	@field:SerializedName("coverPictureUrl")
	val coverPictureUrl: String,

	@field:SerializedName("description")
	val description: String,

	@field:SerializedName("location")
	val location: String,

	@field:SerializedName("followersCount")
	val followersCount: Int,

	@field:SerializedName("email")
	val email: String
) : Parcelable

@Parcelize
data class Data(

	@field:SerializedName("followersCount")
	val followersCount: Int,

	@field:SerializedName("user")
	val user: UserDetail,

	@field:SerializedName("posts")
	val posts: List<PostsItem>,

	@field:SerializedName("followingCount")
	val followingCount: Int
) : Parcelable

@Parcelize
data class PostsItem(

	@field:SerializedName("likesCount")
	val likesCount: Int,

	@field:SerializedName("titleArray")
	val titleArray: List<String>,

	@field:SerializedName("imageUrls")
	val imageUrls: List<String>,

	@field:SerializedName("createdTimestamp")
	val createdTimestamp: Long,

	@field:SerializedName("postId")
	val postId: String,

	@field:SerializedName("text")
	val text: String,

	@field:SerializedName("title")
	val title: String,

	@field:SerializedName("email")
	val email: String,

	@field:SerializedName("timestamp")
	val timestamp: String
) : Parcelable
