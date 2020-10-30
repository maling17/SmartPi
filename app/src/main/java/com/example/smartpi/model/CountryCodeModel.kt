package com.example.smartpi.model

import com.google.gson.annotations.SerializedName

data class CountryCodeModel(

	@field:SerializedName("data")
	val data: List<DataItem?>? = null
)

data class DataItem(

	@field:SerializedName("numcode")
	val numcode: Int? = null,

	@field:SerializedName("iso")
	val iso: String? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("nicename")
	val nicename: String? = null,

	@field:SerializedName("phonecode")
	val phonecode: Int? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("iso3")
	val iso3: String? = null
)
