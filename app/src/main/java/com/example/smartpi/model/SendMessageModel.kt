package com.example.smartpi.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class SendMessageModel(

    @field:SerializedName("data")
    val data: ChatData? = null
) : Parcelable

@Parcelize
data class ChatData(

    @field:SerializedName("user_id")
    val userId: Int? = null,

    @field:SerializedName("created_at")
    val createdAt: String? = null,

    @field:SerializedName("message")
    val message: String? = null,

    @field:SerializedName("username")
    val username: String? = null
) : Parcelable
