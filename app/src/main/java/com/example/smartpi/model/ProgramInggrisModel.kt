package com.example.smartpi.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ProgramInggrisModel(

    @field:SerializedName("data")
    val data: InggrisData? = null
) : Parcelable

@Parcelize
data class InggrisData(

    @field:SerializedName("guru")
    val guru: List<InggrisGuruItem?>? = null,

    @field:SerializedName("desc")
    val desc: InggrisDesc? = null
) : Parcelable

@Parcelize
data class InggrisDesc(

    @field:SerializedName("owner")
    val owner: String? = null,

    @field:SerializedName("duration")
    val duration: Int? = null,

    @field:SerializedName("img")
    val img: String? = null,

    @field:SerializedName("updated_at")
    val updatedAt: String? = null,

    @field:SerializedName("name")
    val name: String? = null,

    @field:SerializedName("created_at")
    val createdAt: String? = null,

    @field:SerializedName("id")
    val id: Int? = null,

    @field:SerializedName("avatar")
    val avatar: String? = null,

    @field:SerializedName("lang")
    val lang: String? = null,

    @field:SerializedName("product_description")
    val productDescription: String? = null,

    @field:SerializedName("desc")
    val desc: String? = null
) : Parcelable

@Parcelize
data class InggrisGuruItem(

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
