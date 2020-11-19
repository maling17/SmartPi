package com.example.smartpi.api

import com.example.smartpi.model.CreateScheduleModel
import com.example.smartpi.model.ScheduleModel
import com.google.gson.annotations.SerializedName
import retrofit2.Response
import retrofit2.http.*

interface CreateScheduleApi {


    @FormUrlEncoded
    @POST("schedule/student-create")
    @Headers("Accept: application/json")
    @SerializedName("data")
    suspend fun createScheduleSesi1(
        @Header("Authorization") token: String?,
        @Field("user_available_id") user_available_id: String?,
        @Field("teacher_id") teacher_id: String?,
        @Field("event_id") event_id: String?,
        @Field("schedule_time") schedule_time: String?,
        @Field("status") status: String?
    ): Response<CreateScheduleModel>

    @POST("schedule/create-2")
    @Headers("Accept: application/json")
    @SerializedName("data")
    suspend fun createScheduleSesi2(
        @Header("Authorization") token: String?,
        @Body createSchedule: ScheduleModel
    ): Response<ScheduleModel>
}