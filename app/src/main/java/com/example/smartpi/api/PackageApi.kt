package com.example.smartpi.api

import com.example.smartpi.model.PackageActiveModel
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers

interface PackageApi {

    @GET("getpackage/myavailable")
    @Headers("Accept: application/json")
    suspend fun getActivePackage(@Header("Authorization") token: String?): Response<PackageActiveModel>
}