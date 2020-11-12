package com.example.smartpi.model

import com.google.gson.annotations.SerializedName

data class SesiModel(

    @field:SerializedName("data")
    val data: SesiData? = null
)

data class SesiData(

    @field:SerializedName("available")
    val available: Int? = null
)
