package com.example.smartpi.api

import com.example.smartpi.model.ResetPasswordModel
import com.example.smartpi.model.UbahPasswordModel
import com.example.smartpi.model.VerifikasiLupaPasswordModel
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface LupaPasswordApi {

    @FormUrlEncoded
    @POST("user/reset-password")
    suspend fun resetPassword(
        @Field("country_code") country_code: String?,
        @Field("phone") phone: String?
    ): Response<ResetPasswordModel?>?

    @FormUrlEncoded
    @POST("user/verifikasi/reset")
    suspend fun verifikasiKodeLupaPassword(
        @Field("phone") phone: String?,
        @Field("activation_code") activation_code: String?
    ): Response<VerifikasiLupaPasswordModel?>?

    @FormUrlEncoded
    @POST("user/new-password")
    suspend fun ubahPassword(
        @Field("phone") phone: String?,
        @Field("password") password: String?,
        @Field("password_c") password_c: String?
    ): Response<UbahPasswordModel?>?


}