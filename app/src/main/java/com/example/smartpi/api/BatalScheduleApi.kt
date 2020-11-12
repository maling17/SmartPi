package com.example.smartpi.api

import com.example.smartpi.model.CancelJadwalModel
import com.google.gson.annotations.SerializedName
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.Path

interface BatalScheduleApi {

    @GET("schedule/student-cancel/{id}")
    @Headers("Accept: application/json")
    @SerializedName("data")
    suspend fun cancelJadwal(
        @Header("Authorization") token: String?,
        @Path("id") id: String?
    ): Response<CancelJadwalModel>

}