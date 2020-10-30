package com.example.smartpi.api

import com.example.smartpi.model.InputNumberModel
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface ResendActivationApi {

    @FormUrlEncoded
    @POST("auth/register/resend-code")
    suspend fun resendCode(
        @Field("phone") phone: String?,
    ): Response<InputNumberModel?>?
}