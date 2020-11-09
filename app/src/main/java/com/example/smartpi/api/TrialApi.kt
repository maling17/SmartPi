package com.example.smartpi.api

import com.example.smartpi.model.PilihTrialModel
import com.example.smartpi.model.PromoModel
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers

interface TrialApi {

    @GET("package/get-trial")
    @Headers("Accept: application/json")
    suspend fun getListTrial(@Header("Authorization") token: String?): Response<PilihTrialModel>
}