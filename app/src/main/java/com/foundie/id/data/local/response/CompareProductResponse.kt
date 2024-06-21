package com.foundie.id.data.local.response

import kotlinx.parcelize.Parcelize
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

@Parcelize
data class CompareProductResponse(

	@field:SerializedName("product")
	val product: Product,

	@field:SerializedName("similarProducts")
	val similarProducts: List<SimilarProductsItem>,

	@field:SerializedName("error")
	val error: Boolean,

	@field:SerializedName("brandCounts")
	val brandCounts: BrandCounts,

	@field:SerializedName("status")
	val status: String
) : Parcelable

@Parcelize
data class SimilarProductsItem(

	@field:SerializedName("Brand")
	val brand: String,

	@field:SerializedName("Type")
	val type: String,

	@field:SerializedName("Variant Name")
	val variantName: String,

	@field:SerializedName("Color HEX")
	val colorHEX: String,

	@field:SerializedName("Similarity")
	val similarity: String,

	@field:SerializedName("Product Title")
	val productTitle: String,

	@field:SerializedName("Season 1 Name")
	val season1Name: String,

	@field:SerializedName("Shade")
	val shade: String,

	@field:SerializedName("Image")
	val image: String

) : Parcelable

@Parcelize
data class Product(

	@field:SerializedName("Brand")
	val brand: String,

	@field:SerializedName("Variant Name")
	val variantName: String,

	@field:SerializedName("Color HEX")
	val colorHEX: String,

	@field:SerializedName("Type")
	val type: String,

	@field:SerializedName("Product Title")
	val productTitle: String,

	@field:SerializedName("Season 1 Name")
	val season1Name: String,

	@field:SerializedName("Shade")
	val shade: String
) : Parcelable

@Parcelize
data class BrandCounts(

	@field:SerializedName("wardah")
	val wardah: Int
) : Parcelable
