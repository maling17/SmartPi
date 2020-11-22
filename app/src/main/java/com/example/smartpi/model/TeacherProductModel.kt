package com.example.smartpi.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class TeacherProductModel(

    @field:SerializedName("teacher")
    var teacher: List<TeacherItem?>? = null
) : Parcelable

@Parcelize
data class TeacherItem(

    @field:SerializedName("name")
    var name: String? = null,

    @field:SerializedName("rating")
    var rating: String? = null,

    @field:SerializedName("id")
    var id: Int? = null,

    @field:SerializedName("avatar")
    var avatar: String? = null,

    @field:SerializedName("recommended")
    var recommended: Int? = null
) : Parcelable
