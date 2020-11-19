package com.example.smartpi.api

import com.example.smartpi.model.HistoryModel
import com.example.smartpi.model.RateModel
import com.example.smartpi.model.WaitingConfirmationModel
import com.google.gson.annotations.SerializedName
import retrofit2.Response
import retrofit2.http.*

interface AfterClassApi {


    @GET("schedule/waiting/confimation")
    @Headers("Accept: application/json")
    @SerializedName("data")
    suspend fun getWaitingConfirmation(@Header("Authorization") token: String?): Response<WaitingConfirmationModel>

    @GET("schedule/history/list")
    @Headers("Accept: application/json")
    @SerializedName("data")
    suspend fun getHistory(@Header("Authorization") token: String?): Response<HistoryModel>

    @FormUrlEncoded
    @POST("teacher/rate")
    @Headers("Accept: application/json")
    @SerializedName("data")
    suspend fun rateClass(
        @Header("Authorization") token: String?,
        @Field("scheduleId") scheduleId: String?,
        @Field("rateTeacher") rateTeacher: String?,
        @Field("feedback") feedback: String?,

        ): Response<RateModel>

}