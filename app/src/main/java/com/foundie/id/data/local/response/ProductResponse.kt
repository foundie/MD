package com.foundie.id.data.local.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

data class ProductResponse(
    @field:SerializedName("error")
    val error: Boolean,

    @field:SerializedName("status")
    val message: String,

    @field:SerializedName("data")
    val data: List<ProductData>
)

@Suppress("DEPRECATED_ANNOTATION")
@Parcelize
data class ProductData(
    @field:SerializedName("Image")
    val image: String,

    @field:SerializedName("Brand")
    val brand: String,

    @field:SerializedName("Product Title")
    val productTitle: String,

    @field:SerializedName("Variant Name")
    val variantName: String,

    @field:SerializedName("Type")
    val type: String,

    @field:SerializedName("Color Hex")
    val colorHex: String,

    @field:SerializedName("Color RGB")
    val colorRGB: String,

    @field:SerializedName("Season 1 Name")
    val season1Name: String,

    @field:SerializedName("Season 1 Percent")
    val season1Percent: Double,

    @field:SerializedName("Season 2 Name")
    val season2Name: String,

    @field:SerializedName("Season 2 Percent")
    val season2Percent: Double,

    @field:SerializedName("Product URL")
    val productURL: String
) : Parcelable
