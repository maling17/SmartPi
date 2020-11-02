package com.example.smartpi.model

import com.google.gson.annotations.SerializedName

data class SignInModel(

	@field:SerializedName("data")
	val data: SignInData? = null,

	@field:SerializedName("meta")
	val meta: Meta? = null
)

data class Meta(

	@field:SerializedName("token")
	val token: String? = null
)

data class SignInData(

	@field:SerializedName("skype_id")
	val skypeId: Any? = null,

	@field:SerializedName("nik")
	val nik: Any? = null,

	@field:SerializedName("role")
	val role: String? = null,

	@field:SerializedName("activation_code")
	val activationCode: String? = null,

	@field:SerializedName("phone")
	val phone: String? = null,

	@field:SerializedName("city")
	val city: Any? = null,

	@field:SerializedName("registered")
	val registered: String? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("email")
	val email: String? = null,

	@field:SerializedName("username")
	val username: String? = null,

	@field:SerializedName("status")
	val status: Int? = null
)
