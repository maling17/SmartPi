package com.example.smartpi.api

import com.example.smartpi.model.CheckEmailModel
import com.example.smartpi.model.NotifikasiModel
import com.example.smartpi.model.UpdateNotifikasiModel
import com.example.smartpi.model.UserModel
import retrofit2.Response
import retrofit2.http.*

interface UserApi {


    @GET("user/profile")
    @Headers("Accept: application/json")
    suspend fun getUser(@Header("Authorization") token: String?): Response<UserModel>

    @GET("user/notifikasi")
    @Headers("Accept: application/json")
    suspend fun getNotif(@Header("Authorization") token: String?): Response<NotifikasiModel>

    @GET("user/notifikasi/update")
    @Headers("Accept: application/json")
    suspend fun getUpdateNotif(@Header("Authorization") token: String?): Response<UpdateNotifikasiModel>


    @GET("user/check-email/generate")
    @Headers("Accept: application/json")
    suspend fun checkEmail(@Header("Authorization") token: String?): Response<CheckEmailModel>

    @FormUrlEncoded
    @POST("user/update/email")
    @Headers("Accept: application/json")
    suspend fun updateEmail(
        @Header("Authorization") token: String?,
        @Field("email") email: String?
    ): Response<CheckEmailModel>

}