package com.example.smartpi.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class DetailTeacherModel(

	@field:SerializedName("data")
	val data: DetailTeacherData? = null
) : Parcelable

@Parcelize
data class DetailTeacherData(

	@field:SerializedName("is_recommended")
	val isRecommended: Int? = null,

	@field:SerializedName("user_id")
	val userId: Int? = null,

	@field:SerializedName("profile")
	val profile: String? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("avatar")
	val avatar: String? = null
) : Parcelable
