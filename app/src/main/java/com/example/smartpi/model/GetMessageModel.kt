package com.example.smartpi.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class GetMessageModel(

    @field:SerializedName("data")
    val data: List<ChatDataItem?>? = null
) : Parcelable

@Parcelize
data class ChatDataItem(

    @field:SerializedName("user_id")
    val userId: Int? = null,

    @field:SerializedName("created_at")
    val createdAt: String? = null,

    @field:SerializedName("message")
    val message: String? = null,

    @field:SerializedName("username")
    val username: String? = null
) : Parcelable {
    companion object {
        const val TYPE_MY_MESSAGE = 0
        const val TYPE_FRIEND_MESSAGE = 1
    }
}
