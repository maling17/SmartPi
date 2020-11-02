package com.example.smartpi.model

import com.google.gson.annotations.SerializedName

data class ResetPasswordModel(

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("email")
	val email: String? = null
)
