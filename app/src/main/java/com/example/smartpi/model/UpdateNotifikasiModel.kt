package com.example.smartpi.model

import com.google.gson.annotations.SerializedName

data class UpdateNotifikasiModel(

    @field:SerializedName("data")
    val data: Int? = null,

    @field:SerializedName("message")
    val message: String? = null
)
