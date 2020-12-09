package com.example.smartpi.api

import com.example.smartpi.model.CheckVersionModel
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Headers

interface CheckVersiApi {

    @GET("check-version")
    @Headers("Accept: application/json")
    suspend fun getVersion(): Response<CheckVersionModel>
}