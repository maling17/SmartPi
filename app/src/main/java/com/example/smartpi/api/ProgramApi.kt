package com.example.smartpi.api

import com.example.smartpi.model.MatematikaModel
import com.example.smartpi.model.ProgramInggrisModel
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

    @GET("program/akademik")
    @Headers("Accept: application/json")
    suspend fun getProgramMatematika(): Response<MatematikaModel>

    @GET("program/bing")
    @Headers("Accept: application/json")
    suspend fun getProgramInggris(): Response<ProgramInggrisModel>

    @GET("program/ngaji")
    @Headers("Accept: application/json")
    suspend fun getProgramMengaji(): Response<ProgramInggrisModel>

}