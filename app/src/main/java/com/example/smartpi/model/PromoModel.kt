package com.example.smartpi.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class PromoModel(

    @field:SerializedName("data")
    val data: List<PromoItem?>? = null
) : Parcelable

@Parcelize
data class PromoItem(

    @field:SerializedName("end_date")
    val endDate: String? = null,

    @field:SerializedName("id")
    val id: Int? = null,

    @field:SerializedName("title")
    val title: String? = null,

    @field:SerializedName("gambar")
    val gambar: String? = null,

    @field:SerializedName("content")
    val content: String? = null,

    @field:SerializedName("start_date")
    val startDate: String? = null
) : Parcelable
