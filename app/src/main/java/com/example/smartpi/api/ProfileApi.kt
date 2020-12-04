package com.example.smartpi.api

import com.example.smartpi.model.ProfileModel
import com.example.smartpi.model.UpdatePasswordModel
import com.google.gson.annotations.SerializedName
import retrofit2.Response
import retrofit2.http.*

interface ProfileApi {
    @FormUrlEncoded
    @POST("user/update/profile")
    @Headers("Accept: application/json")
    @SerializedName("data")
    suspend fun ubahProfile(
        @Header("Authorization") token: String?,
        @Field("name") name: String
    ): Response<ProfileModel>

    @FormUrlEncoded
    @POST("auth/password/update")
    @Headers("Accept: application/json")
    @SerializedName("data")
    suspend fun ubahKataSandi(
        @Header("Authorization") token: String?,
        @Field("old_password") old_password: String,
        @Field("new_password") new_password: String,
        @Field("new_password_c") new_password_c: String
    ): Response<UpdatePasswordModel>
}