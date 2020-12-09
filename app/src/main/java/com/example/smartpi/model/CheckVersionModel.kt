package com.example.smartpi.model

import com.google.gson.annotations.SerializedName

data class CheckVersionModel(

    @field:SerializedName("data")
    val data: List<VersiItem?>? = null
)

data class VersiItem(

    @field:SerializedName("version_name")
    val versionName: String? = null,

    @field:SerializedName("version_code")
    val versionCode: Int? = null,

    @field:SerializedName("id")
    val id: Int? = null
)
