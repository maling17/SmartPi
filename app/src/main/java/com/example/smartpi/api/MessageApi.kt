package com.example.smartpi.api

import com.example.smartpi.model.GetMessageModel
import com.example.smartpi.model.SendMessageModel
import com.google.gson.annotations.SerializedName
import retrofit2.Response
import retrofit2.http.*

interface MessageApi {

    @GET("message/{schedule_id}")
    @Headers("Accept: application/json")
    @SerializedName("data")
    suspend fun getMessage(
        @Header("Authorization") token: String?,
        @Path("schedule_id") schedule_id: String?,
    ): Response<GetMessageModel>


    @FormUrlEncoded
    @POST("message/sent")
    @Headers("Accept: application/json")
    @SerializedName("data")
    suspend fun sendMessage(
        @Header("Authorization") token: String?,
        @Field("schedule_id") schedule_id: String?,
        @Field("message") message: String?,

        ): Response<SendMessageModel>

}