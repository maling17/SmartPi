package com.example.smartpi.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ListGroupClassModel(

    @field:SerializedName("data")
    val data: List<ListGroupItem?>? = null
) : Parcelable

@Parcelize
data class ListGroupItem(

    @field:SerializedName("duration")
    val duration: Int? = null,

    @field:SerializedName("img")
    val img: String? = null,

    @field:SerializedName("teacher")
    val teacher: String? = null,

    @field:SerializedName("nama_kelas")
    val namaKelas: String? = null,

    @field:SerializedName("teacher_id")
    val teacherId: String? = null,

    @field:SerializedName("price")
    val price: String? = null,

    @field:SerializedName("kuota")
    val kuota: String? = null,

    @field:SerializedName("id")
    val id: Int? = null,

    @field:SerializedName("type")
    val type: Int? = null,

    @field:SerializedName("sisa_kuota")
    val sisaKuota: Int? = null,

    @field:SerializedName("desc")
    val desc: String? = null
) : Parcelable
