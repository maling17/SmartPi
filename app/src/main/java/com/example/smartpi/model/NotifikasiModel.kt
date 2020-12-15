package com.example.smartpi.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class NotifikasiModel(

    @field:SerializedName("data")
    val data: NotifikasiData? = null
) : Parcelable

@Parcelize
data class NotifikasiData(

    @field:SerializedName("notif")
    val notif: List<NotifItem?>? = null,

    @field:SerializedName("detail")
    val detail: Detail? = null
) : Parcelable

@Parcelize
data class NotifItem(

    @field:SerializedName("user_id")
    val userId: Int? = null,

    @field:SerializedName("show")
    val show: Int? = null,

    @field:SerializedName("id")
    val id: Int? = null,

    @field:SerializedName("message")
    val message: String? = null,

    @field:SerializedName("schedule_id")
    val scheduleId: Int? = null
) : Parcelable

@Parcelize
data class Detail(

    @field:SerializedName("new")
    val jsonMemberNew: Int? = null
) : Parcelable
