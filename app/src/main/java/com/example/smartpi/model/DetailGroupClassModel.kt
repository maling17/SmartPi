package com.example.smartpi.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class DetailGroupClassModel(

    @field:SerializedName("data")
    val data: DetailData? = null
) : Parcelable

@Parcelize
data class ScheduleDetailItem(

    @field:SerializedName("schedule_time")
    val scheduleTime: String? = null,

    @field:SerializedName("class_id")
    val classId: Int? = null,

    @field:SerializedName("room_code")
    val roomCode: String? = null,

    @field:SerializedName("schedule_end")
    val scheduleEnd: String? = null,

    @field:SerializedName("id")
    val id: Int? = null,

    @field:SerializedName("platform")
    val platform: Int? = null
) : Parcelable

@Parcelize
data class KelasItem(

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

    @field:SerializedName("desc")
    val desc: String? = null,

    @field:SerializedName("gambar")
    val gambar: String? = null
) : Parcelable

@Parcelize
data class DetailData(

    @field:SerializedName("schedule")
    var schedule: List<ScheduleDetailItem?>? = null,

    @field:SerializedName("kelas")
    var kelas: List<KelasItem?>? = null
) : Parcelable
