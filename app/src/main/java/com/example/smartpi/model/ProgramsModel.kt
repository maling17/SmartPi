package com.example.smartpi.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ProgramModel(

    @field:SerializedName("data")
    val data: List<ProgramsItems?>? = null
) : Parcelable

@Parcelize
data class ProgramsItems(

    @field:SerializedName("img")
    val img: String? = null,

    @field:SerializedName("name")
    val name: String? = null,

    @field:SerializedName("id")
    val id: Int? = null
) : Parcelable
