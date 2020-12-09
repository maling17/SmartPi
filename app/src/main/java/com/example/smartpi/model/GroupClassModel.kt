package com.example.smartpi.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class GroupClassModel(

    @field:SerializedName("data")
    val data: List<GroupItem?>? = null,
) : Parcelable

@Parcelize
data class GroupItem(

    @field:SerializedName("duration")
    var duration: Int? = null,

    @field:SerializedName("schedule")
    var schedule: List<ScheduleItem?>? = null,

    @field:SerializedName("img")
    val img: String? = null,

    @field:SerializedName("teacher")
    var teacher: String? = null,

    @field:SerializedName("nama_kelas")
    var namaKelas: String? = null,

    @field:SerializedName("teacher_id")
    var teacherId: String? = null,

    @field:SerializedName("price")
    val price: String? = null,

    @field:SerializedName("kuota")
    val kuota: String? = null,

    @field:SerializedName("id")
    val id: Int? = null,

    @field:SerializedName("type")
    val type: Int? = null,

    @field:SerializedName("desc")
    val desc: String? = null
) : Parcelable

@Parcelize
data class ScheduleItem(

    @field:SerializedName("schedule_time")
    var scheduleTime: String? = null,

    @field:SerializedName("class_id")
    val classId: Int? = null,

    @field:SerializedName("room_code")
    var roomCode: String? = null,

    @field:SerializedName("schedule_end")
    var scheduleEnd: String? = null,

    @field:SerializedName("id")
    val id: Int? = null,

    @field:SerializedName("platform")
    var platform: Int? = null
) : Parcelable
