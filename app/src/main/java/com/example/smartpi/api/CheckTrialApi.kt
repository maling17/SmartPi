package com.example.smartpi.api

import com.example.smartpi.model.CheckTrialModel
import com.example.smartpi.model.UserModel
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers

interface CheckTrialApi {

    @GET("package/check-trial")
    @Headers("Accept: application/json")
    suspend fun getCheckTrial(@Header("Authorization") token:String?): Response<CheckTrialModel>

}