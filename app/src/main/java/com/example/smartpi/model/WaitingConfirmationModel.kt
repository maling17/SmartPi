package com.example.smartpi.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class WaitingConfirmationModel(

    @field:SerializedName("data")
    val data: List<WaitingItem?>? = null
) : Parcelable

@Parcelize
data class WaitingItem(

    @field:SerializedName("teacher_name")
    val teacherName: String? = null,

    @field:SerializedName("student")
    val student: String? = null,

    @field:SerializedName("user_package")
    val userPackage: Int? = null,

    @field:SerializedName("teacher_id")
    val teacherId: Int? = null,

    @field:SerializedName("level_id")
    val levelId: String? = null,

    @field:SerializedName("room_code")
    val roomCode: String? = null,

    @field:SerializedName("package_id")
    val packageId: Int? = null,

    @field:SerializedName("platform")
    val platform: Int? = null,

    @field:SerializedName("duration")
    val duration: Int? = null,

    @field:SerializedName("on_going")
    val onGoing: Int? = null,

    @field:SerializedName("teacher_rate")
    val teacherRate: String? = null,

    @field:SerializedName("student_skype_id")
    val studentSkypeId: String? = null,

    @field:SerializedName("session_complete")
    val sessionComplete: Int? = null,

    @field:SerializedName("id")
    val id: Int? = null,

    @field:SerializedName("student_email")
    val studentEmail: String? = null,

    @field:SerializedName("level")
    val level: String? = null,

    @field:SerializedName("teacher_avatar")
    val teacherAvatar: String? = null,

    @field:SerializedName("student_id")
    val studentId: Int? = null,

    @field:SerializedName("teacher_feedback")
    val teacherFeedback: String? = null,

    @field:SerializedName("teacher_skype_id")
    val teacherSkypeId: String? = null,

    @field:SerializedName("teacher_email")
    val teacherEmail: String? = null,

    @field:SerializedName("schedule_time")
    val scheduleTime: String? = null,

    @field:SerializedName("package_name")
    val packageName: String? = null,

    @field:SerializedName("session_available")
    val sessionAvailable: Int? = null,

    @field:SerializedName("schedule_end")
    val scheduleEnd: String? = null,

    @field:SerializedName("teacher_origin")
    val teacherOrigin: String? = null,

    @field:SerializedName("status")
    val status: Int? = null
) : Parcelable
