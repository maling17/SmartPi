package com.example.smartpi.api

import com.example.smartpi.model.InputNumberModel
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface InputNumberApi {

    @FormUrlEncoded
    @POST("auth/check-phone/new")
    suspend fun inputNumber(
        @Field("phone_number") phone_number: String?,
        @Field("country_code") country_code: String?,
    ): Response<InputNumberModel?>?
}