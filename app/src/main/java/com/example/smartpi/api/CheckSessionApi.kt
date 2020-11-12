package com.example.smartpi.api

import com.example.smartpi.model.CheckSessionModel
import com.google.gson.annotations.SerializedName
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.Path

interface CheckSessionApi {

    @GET("package/check-session/{id}")
    @Headers("Accept: application/json")
    @SerializedName("data")
    suspend fun checkSession(
        @Header("Authorization") token: String?,
        @Path("id") id: String?
    ): Response<CheckSessionModel>
}