package com.example.smartpi.api

import com.example.smartpi.model.CreateScheduleModel
import com.example.smartpi.model.DetailGroupClassModel
import com.example.smartpi.model.ListGroupClassModel
import com.google.gson.annotations.SerializedName
import retrofit2.Response
import retrofit2.http.*

interface GroupClassApi {

    @GET("group/schedule/all")
    @Headers("Accept: application/json")
    @SerializedName("data")
    suspend fun getGroupClass(): Response<ListGroupClassModel>

    @GET("group/schedule/{id}")
    @Headers("Accept: application/json")
    @SerializedName("data")
    suspend fun getDetailGroupClass(@Path("id") id: Int): Response<DetailGroupClassModel>

    @FormUrlEncoded
    @POST("group/schedule/create")
    @Headers("Accept: application/json")
    @SerializedName("data")
    suspend fun createGroupSchedule(
        @Header("Authorization") token: String?,
        @Field("user_available_id") user_available_id: String?,
        @Field("class_id") class_id: String?
    ): Response<CreateScheduleModel>

}