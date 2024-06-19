package com.foundie.id.data.local.response

import kotlinx.parcelize.Parcelize
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

@Parcelize
data class FilterProductResponse(

	@field:SerializedName("data")
	val data: List<DataFilterProduct>,

	@field:SerializedName("error")
	val error: Boolean,

	@field:SerializedName("status")
	val status: String
) : Parcelable

@Parcelize
data class DataFilterProduct(

	@field:SerializedName("S2 Closest Color")
	val s2ClosestColor: String,

	@field:SerializedName("Product Title")
	val productTitle: String,

	@field:SerializedName("Season 1 Name")
	val season1Name: String,

	@field:SerializedName("Index")
	val index: Int,

	@field:SerializedName("Image")
	val image: String,

	@field:SerializedName("Brand")
	val brand: String,

	@field:SerializedName("Variant Name")
	val variantName: String,

	@field:SerializedName("Type")
	val type: String,

	@field:SerializedName("Color Hex")
	val colorHex: String,

	@field:SerializedName("Season 2 Name")
	val season2Name: String,

	@field:SerializedName("Product URL")
	val productURL: String,

	@field:SerializedName("S1 Closest Color")
	val s1ClosestColor: String,

	@field:SerializedName("Color RGB")
	val colorRGB: String
) : Parcelable
