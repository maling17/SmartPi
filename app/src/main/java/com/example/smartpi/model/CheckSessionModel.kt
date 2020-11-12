package com.example.smartpi.model

import com.google.gson.annotations.SerializedName

data class CheckSessionModel(

    @field:SerializedName("data")
    val data: SessionData? = null
)

data class SessionData(

    @field:SerializedName("available")
    val available: String? = null
)
