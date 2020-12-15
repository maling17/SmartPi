package com.example.smartpi.api

import com.example.smartpi.adapter.AmbilTrialModel
import com.example.smartpi.model.PilihTrialModel
import retrofit2.Response
import retrofit2.http.*

interface TrialApi {

    @GET("package/get-trial")
    @Headers("Accept: application/json")
    suspend fun getListTrial(@Header("Authorization") token: String?): Response<PilihTrialModel>

    @FormUrlEncoded
    @POST("package/select-trial")
    @Headers("Accept: application/json")
    suspend fun ambilTrial(
        @Header("Authorization") token: String?,
        @Field("package_id") package_id: String
    ): Response<AmbilTrialModel>
}