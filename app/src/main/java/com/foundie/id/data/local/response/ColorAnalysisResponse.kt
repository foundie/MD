package com.foundie.id.data.local.response

import kotlinx.parcelize.Parcelize
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

@Parcelize
data class ColorAnalysisResponse(

	@field:SerializedName("data")
	val data: DataAnalysis,

	@field:SerializedName("message")
	val message: String,

	@field:SerializedName("error")
	val error: Boolean,

	@field:SerializedName("status")
	val status: Int
) : Parcelable

@Parcelize
data class SeasonCompatibilityPercentages(

	@field:SerializedName("Autumn Soft")
	val autumnSoft: Int,

	@field:SerializedName("Spring Warm")
	val springWarm: Int,

	@field:SerializedName("Summer Cool")
	val summerCool: Int,

	@field:SerializedName("Spring Clear")
	val springClear: Int,

	@field:SerializedName("Spring Light")
	val springLight: Int,

	@field:SerializedName("Summer Soft")
	val summerSoft: Int,

	@field:SerializedName("Autumn Warm")
	val autumnWarm: Int,

	@field:SerializedName("Summer Light")
	val summerLight: Int,

	@field:SerializedName("Winter Clear")
	val winterClear: Int,

	@field:SerializedName("Autumn Deep")
	val autumnDeep: Int,

	@field:SerializedName("Winter Cool")
	val winterCool: Int,

	@field:SerializedName("Winter Deep")
	val winterDeep: Int
) : Parcelable

@Parcelize
data class DataAnalysis(

	@field:SerializedName("dominantCharacteristic")
	val dominantCharacteristic: String,

	@field:SerializedName("seasonImage")
	val seasonImage: String,

	@field:SerializedName("colorSeason")
	val colorSeason: String,

	@field:SerializedName("description")
	val description: String,

	@field:SerializedName("seasonCompatibilityPercentages")
	val seasonCompatibilityPercentages: SeasonCompatibilityPercentage,

	@field:SerializedName("palette")
	val palette: List<String>,

	@field:SerializedName("secondaryCharacteristic")
	val secondaryCharacteristic: String,

	@field:SerializedName("type")
	val type: String
) : Parcelable
