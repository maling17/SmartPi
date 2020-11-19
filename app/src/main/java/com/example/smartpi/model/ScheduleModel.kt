package com.example.smartpi.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ScheduleModel(

	@field:SerializedName("user_available_id")
	var userAvailableId: String? = null,

	@field:SerializedName("teacher_id")
	var teacherId: String? = null,

	@field:SerializedName("schedule")
	var schedule: ArrayList<Schedule?>? = null


) : Parcelable

@Parcelize
data class Schedule(

	@field:SerializedName("event_id")
	var eventId: String? = null,

	@field:SerializedName("schedule_time")
	var scheduleTime: String? = null,

	@field:SerializedName("status")
	var status: String? = null
) : Parcelable

