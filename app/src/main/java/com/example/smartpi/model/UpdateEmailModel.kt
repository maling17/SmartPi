package com.example.smartpi.model

import com.google.gson.annotations.SerializedName

data class UpdateEmailModel(

    @field:SerializedName("msg")
    val msg: String? = null,

    @field:SerializedName("status")
    val status: Boolean? = null
)
