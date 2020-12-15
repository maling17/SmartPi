package com.example.smartpi.api

import com.example.smartpi.model.CreateGroupClassModel
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
    @POST("group/buy/discount100")
    @Headers("Accept: application/json")
    @SerializedName("data")
    suspend fun createGroupScheduleFree(
        @Header("Authorization") token: String?,
        @Field("class_id") class_id: Int?
    ): Response<CreateGroupClassModel>

}