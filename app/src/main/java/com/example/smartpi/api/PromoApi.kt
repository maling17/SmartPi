package com.example.smartpi.api

import com.example.smartpi.model.PromoModel
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers

interface PromoApi {

    @GET("promo-smartpi")
    @Headers("Accept: application/json")
    suspend fun getPromo(@Header("Authorization") token: String?): Response<PromoModel>
}
