package com.example.smartpi.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class MatematikaModel(

    @field:SerializedName("data")
    val data: MatematikaData? = null
) : Parcelable

@Parcelize
data class Sd(

    @field:SerializedName("guru")
    val guru: List<GuruItem?>? = null,

    @field:SerializedName("desc")
    val desc: Desc? = null
) : Parcelable

@Parcelize
data class MatematikaData(

    @field:SerializedName("sd")
    val sd: Sd? = null,

    @field:SerializedName("tk")
    val tk: Tk? = null,

    @field:SerializedName("smp")
    val smp: Smp? = null
) : Parcelable

@Parcelize
data class Smp(

    @field:SerializedName("guru")
    val guru: List<GuruItem?>? = null,

    @field:SerializedName("desc")
    val desc: Desc? = null
) : Parcelable

@Parcelize
data class Desc(

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
data class GuruItem(

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

@Parcelize
data class Tk(

    @field:SerializedName("guru")
    val guru: List<GuruItem?>? = null,

    @field:SerializedName("desc")
    val desc: Desc? = null
) : Parcelable
