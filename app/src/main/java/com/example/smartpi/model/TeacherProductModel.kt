package com.example.smartpi.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class TeacherProductModel(

    @field:SerializedName("teacher")
    val teacher: List<TeacherItem?>? = null
) : Parcelable

@Parcelize
data class TeacherItem(

    @field:SerializedName("name")
    val name: String? = null,

    @field:SerializedName("rating")
    val rating: String? = null,

    @field:SerializedName("id")
    val id: Int? = null,

    @field:SerializedName("avatar")
    val avatar: String? = null,

    @field:SerializedName("recommended")
    val recommended: Int? = null
) : Parcelable
