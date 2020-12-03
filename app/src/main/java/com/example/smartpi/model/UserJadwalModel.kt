package com.example.smartpi.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

data class UserJadwalModel(

    @field:SerializedName("data")
    var data: List<JadwalItem?>? = null
)
@Parcelize
data class JadwalItem(

    @field:SerializedName("teacher_name")
    var teacherName: String? = null,

    @field:SerializedName("student")
    var student: String? = null,

    @field:SerializedName("user_package")
    var userPackage: String? = null,

    @field:SerializedName("teacher_id")
    var teacherId: String? = null,

    @field:SerializedName("level_id")
    var levelId: String? = null,

    @field:SerializedName("room_code")
    var roomCode: String? = null,

    @field:SerializedName("package_id")
    var packageId: String? = null,

    @field:SerializedName("platform")
    var platform: String? = null,

    @field:SerializedName("duration")
    var duration: String? = null,

    @field:SerializedName("on_going")
    var onGoing: String? = null,

    @field:SerializedName("teacher_rate")
    var teacherRate: String? = null,

    @field:SerializedName("student_skype_id")
    var studentSkypeId: String? = null,

    @field:SerializedName("session_complete")
    var sessionComplete: String? = null,

    @field:SerializedName("id")
    var id: String? = null,

    @field:SerializedName("student_email")
    var studentEmail: String? = null,

    @field:SerializedName("level")
    var level: String? = null,

    @field:SerializedName("teacher_avatar")
    var teacherAvatar: String? = null,

    @field:SerializedName("student_id")
    var studentId: String? = null,

    @field:SerializedName("teacher_feedback")
    var teacherFeedback: String? = null,

    @field:SerializedName("teacher_skype_id")
    var teacherSkypeId: String? = null,

    @field:SerializedName("teacher_email")
    var teacherEmail: String? = null,

    @field:SerializedName("schedule_time")
    var scheduleTime: String? = null,

    @field:SerializedName("package_name")
    var packageName: String? = null,

    @field:SerializedName("session_available")
    var sessionAvailable: String? = null,

    @field:SerializedName("schedule_end")
    var scheduleEnd: String? = null,

    @field:SerializedName("teacher_origin")
    var teacherOrigin: String? = null,

    @field:SerializedName("status")
    var status: String? = null
):Parcelable
