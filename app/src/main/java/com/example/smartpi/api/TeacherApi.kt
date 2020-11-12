package com.example.smartpi.api

import com.example.smartpi.model.TeacherProductModel
import com.example.smartpi.model.TeacherScheduleModel
import com.example.smartpi.model.TeacherScheduleSlotModel
import com.google.gson.annotations.SerializedName
import retrofit2.Response
import retrofit2.http.*

interface TeacherApi {


    @GET("teacher/get-teacher-package/{kode_teacher}")
    @Headers("Accept: application/json")
    @SerializedName("data")
    suspend fun getTeacherProduct(
        @Header("Authorization") token: String?,
        @Path("kode_teacher") kode_teacher: String?
    ): Response<TeacherProductModel>

    @GET("teacher/get-teacher-schedule/{id}")
    @Headers("Accept: application/json")
    @SerializedName("data")
    suspend fun getTeacherSchedule(
        @Header("Authorization") token: String?,
        @Path("id") id: String?
    ): Response<TeacherScheduleModel>


    @FormUrlEncoded
    @POST("teacher/available-date")
    @Headers("Accept: application/json")
    @SerializedName("data")
    suspend fun getTeacherScheduleAvailability(
        @Header("Authorization") token: String?,
        @Field("teacher_id") teacher_id: String?,
        @Field("date") date: String?
    ): Response<TeacherScheduleSlotModel>
}