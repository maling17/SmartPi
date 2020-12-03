package com.example.smartpi.api

import com.example.smartpi.model.SignInModel
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.Headers
import retrofit2.http.POST

interface SignInApi {

    @FormUrlEncoded
    @Headers("Accept: application/json")
    @POST("auth/input-password/new")
    suspend fun checkSignIn(
        @Field("phone") phone: String?,
        @Field("password") password: String?,
    ): Response<SignInModel>?

    @FormUrlEncoded
    @Headers("Accept: application/json")
    @POST("auth/login")
    suspend fun checkSignInEmail(
        @Field("email") email: String?,
        @Field("password") password: String?,
    ): Response<SignInModel>?
}