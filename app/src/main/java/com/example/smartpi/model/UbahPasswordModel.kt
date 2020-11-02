package com.example.smartpi.model

import com.google.gson.annotations.SerializedName

data class UbahPasswordModel(

	@field:SerializedName("data")
	val data: ubahPasswordData? = null,

	@field:SerializedName("success")
	val success: String? = null
)

data class ubahPasswordData(

	@field:SerializedName("role")
	val role: String? = null,

	@field:SerializedName("flag")
	val flag: Int? = null,

	@field:SerializedName("activation_code")
	val activationCode: String? = null,

	@field:SerializedName("city")
	val city: Any? = null,

	@field:SerializedName("api_token")
	val apiToken: String? = null,

	@field:SerializedName("created_at")
	val createdAt: String? = null,

	@field:SerializedName("device_check")
	val deviceCheck: Int? = null,

	@field:SerializedName("skype_id")
	val skypeId: Any? = null,

	@field:SerializedName("nik")
	val nik: Any? = null,

	@field:SerializedName("updated_at")
	val updatedAt: String? = null,

	@field:SerializedName("phone")
	val phone: String? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("email")
	val email: String? = null,

	@field:SerializedName("status")
	val status: Int? = null
)
