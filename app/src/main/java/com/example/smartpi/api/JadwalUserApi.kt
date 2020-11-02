package com.example.smartpi.api

import com.example.smartpi.model.UserJadwalModel
import com.google.gson.annotations.SerializedName
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers

interface JadwalUserApi {


    @GET("schedule/list")
    @Headers("Accept: application/json")
    @SerializedName("data")
    suspend fun getJadwalUser(@Header("Authorization") token: String?): Response<UserJadwalModel>
}