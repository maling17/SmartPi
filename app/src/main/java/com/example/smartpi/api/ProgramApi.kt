package com.example.smartpi.api

import com.example.smartpi.model.ProgramModel
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Headers

interface ProgramApi {


    @GET("program/matematika")
    @Headers("Accept: application/json")
    suspend fun getMatematika(): Response<ProgramModel>

    @GET("program/english")
    @Headers("Accept: application/json")
    suspend fun getInggris(): Response<ProgramModel>

    @GET("program/mengaji")
    @Headers("Accept: application/json")
    suspend fun getMengaji(): Response<ProgramModel>
}