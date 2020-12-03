package com.example.smartpi.api

import com.example.smartpi.model.*
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

    @FormUrlEncoded
    @POST("teacher/filterumum")
    @Headers("Accept: application/json")
    @SerializedName("data")
    suspend fun filterTeacherUmum(
        @Header("Authorization") token: String?,
        @Field("range_akhir") range_akhir: String?,
        @Field("hari") hari: String?,
        @Field("kode_teacher") kode_teacher: String?,
        @Field("timezone") timezone: String?,
        @Field("hayu_atuh") hayu_atuh: String?

    ): Response<FilterUmumModel>

    @FormUrlEncoded
    @POST("teacher/filterkhusus")
    @Headers("Accept: application/json")
    @SerializedName("data")
    suspend fun filterTeacherKhusus(
        @Header("Authorization") token: String?,
        @Field("waktu") waktu: String?,
        @Field("timezone") timezone: String?,
        @Field("kode_teacher") kode_teacher: String?

    ): Response<FilterKhususModel>


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
        @Field("date") date: String?,
        @Field("timezone") timezone: String?
    ): Response<TeacherScheduleSlotModel>
}