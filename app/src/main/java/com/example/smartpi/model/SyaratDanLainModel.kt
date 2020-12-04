package com.example.smartpi.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class SyaratDanLainModel(

    @field:SerializedName("data")
    val data: SyaratData? = null
) : Parcelable

@Parcelize
data class SyaratData(

    @field:SerializedName("id")
    val id: Int? = null,

    @field:SerializedName("judul")
    val judul: String? = null,

    @field:SerializedName("syarat")
    val syarat: String? = null
) : Parcelable
