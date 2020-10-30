package com.example.smartpi.api

import com.example.smartpi.model.InputActivationModel
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface InputActivationApi {
    @FormUrlEncoded
    @POST("auth/activation/new")
    suspend fun inputActivation(
        @Field("phone") phone: String?,
        @Field("activation_code") activation_code: String?,
    ): Response<InputActivationModel>?
}