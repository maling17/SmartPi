package com.example.smartpi.model

import com.google.gson.annotations.SerializedName

data class CreateScheduleModel(

    @field:SerializedName("data")
    val data: ScheduleData? = null,

    @field:SerializedName("warning")
    val warning: Boolean? = null
)

data class ScheduleData(

    @field:SerializedName("room_id")
    val roomId: Int? = null,

    @field:SerializedName("level")
    val level: Any? = null,

    @field:SerializedName("teacher_id")
    val teacherId: Int? = null,

    @field:SerializedName("created_at")
    val createdAt: String? = null,

    @field:SerializedName("platform")
    val platform: Int? = null,

    @field:SerializedName("duration")
    val duration: Int? = null,

    @field:SerializedName("user_available_id")
    val userAvailableId: Int? = null,

    @field:SerializedName("teacher_rate")
    val teacherRate: Any? = null,

    @field:SerializedName("teacher_feedback")
    val teacherFeedback: Any? = null,

    @field:SerializedName("student_feedback")
    val studentFeedback: Any? = null,

    @field:SerializedName("schedule_time")
    val scheduleTime: String? = null,

    @field:SerializedName("updated_at")
    val updatedAt: String? = null,

    @field:SerializedName("schedule_end")
    val scheduleEnd: String? = null,

    @field:SerializedName("id")
    val id: Int? = null,

    @field:SerializedName("is_reschedule")
    val isReschedule: Any? = null,

    @field:SerializedName("status")
    val status: Int? = null
)
