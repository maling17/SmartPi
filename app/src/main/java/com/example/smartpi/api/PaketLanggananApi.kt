package com.example.smartpi.api

import com.example.smartpi.model.DetailPaketModel
import com.example.smartpi.model.PaketLanggananModel
import com.google.gson.annotations.SerializedName
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path

interface PaketLanggananApi {

    @GET("program/paket/{id_paket}")
    @Headers("Accept: application/json")
    @SerializedName("data")
    suspend fun getPricePaket(
        @Path("id_paket") id_paket: String?
    ): Response<PaketLanggananModel>

    @GET("program/paket/detail/{id}")
    @Headers("Accept: application/json")
    @SerializedName("data")
    suspend fun getDetailPaket(
        @Path("id") id: String?
    ): Response<DetailPaketModel>

}