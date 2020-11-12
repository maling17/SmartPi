package com.example.smartpi.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class TeacherScheduleModel(

    @field:SerializedName("availability")
    val availability: List<AvailabilityItem?>? = null,

    @field:SerializedName("status")
    val status: String? = null
) : Parcelable

@Parcelize
data class AvailabilityItem(

    @field:SerializedName("teacher_id")
    val teacherId: Int? = null,

    @field:SerializedName("start")
    val start: String? = null,

    @field:SerializedName("end")
    val end: String? = null,

    @field:SerializedName("id")
    val id: Int? = null,

    @field:SerializedName("time_zone")
    val timeZone: String? = null,

    @field:SerializedName("status")
    val status: Int? = null
) : Parcelable
