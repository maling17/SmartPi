package com.example.smartpi.api

import com.example.smartpi.model.SignUpModel
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.Headers
import retrofit2.http.POST

interface SignUpApi {

    @FormUrlEncoded
    @Headers("Accept: application/json")
    @POST("auth/name/pass/new")
    suspend fun signUp(
        @Field("phone") phone: String?,
        @Field("password") password: String?,
        @Field("name") name: String?,
        @Field("password_c") password_c: String?

    ): Response<SignUpModel>?

}