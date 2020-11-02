package com.example.smartpi.model

import com.google.gson.annotations.SerializedName

data class VerifikasiLupaPasswordModel(

	@field:SerializedName("phone")
	val phone: String? = null,

	@field:SerializedName("message")
	val message: String? = null
)
