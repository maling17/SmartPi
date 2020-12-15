package com.example.smartpi.api


import com.example.smartpi.model.ListGroupClassModel
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Headers

interface PraKerjaApi {

    @GET("group/prakerja/schedule")
    @Headers("Accept: application/json")
    suspend fun getListPrakerja(): Response<ListGroupClassModel>
}