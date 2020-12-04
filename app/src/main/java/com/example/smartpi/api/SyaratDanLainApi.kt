package com.example.smartpi.api

import com.example.smartpi.model.SyaratDanLainModel
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Headers

interface SyaratDanLainApi {

    @GET("syarat-ketentuan")
    @Headers("Accept: application/json")
    suspend fun getSyarat(): Response<SyaratDanLainModel>

    @GET("kebijakan-privasi")
    @Headers("Accept: application/json")
    suspend fun getKebijakan(): Response<SyaratDanLainModel>

    @GET("bantuan")
    @Headers("Accept: application/json")
    suspend fun getBantuan(): Response<SyaratDanLainModel>

    @GET("tentang-kami")
    @Headers("Accept: application/json")
    suspend fun getTentangKami(): Response<SyaratDanLainModel>
}
