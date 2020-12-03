package com.example.smartpi.api


import com.example.smartpi.model.GroupClassModel
import com.example.smartpi.model.UserJadwalModel
import com.google.gson.annotations.SerializedName
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers

interface JadwalUserApi {


    @GET("schedule/list")
    @Headers("Accept: application/json")
    @SerializedName("data")
    suspend fun getJadwalUser(@Header("Authorization") token: String?): Response<UserJadwalModel>

    @GET("group/myschedule")
    @Headers("Accept: application/json")
    @SerializedName("data")
    suspend fun getGroupClass(@Header("Authorization") token: String?): Response<GroupClassModel>

    @GET("schedule/list")
    @Headers("Accept: application/json")
    @SerializedName("data")
    suspend fun getDetailJadwal(
        @Header("Authorization") token: String?,
        @Field("id") id: String?
    ): Response<UserJadwalModel>
}