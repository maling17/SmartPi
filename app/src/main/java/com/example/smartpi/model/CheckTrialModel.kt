package com.example.smartpi.model

import com.google.gson.annotations.SerializedName

data class CheckTrialModel(

	@field:SerializedName("Used")
	val used: Boolean? = null,

	@field:SerializedName("status")
	val status: String? = null
)
