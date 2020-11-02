package com.example.smartpi.api

import com.example.smartpi.model.UserData
import com.example.smartpi.model.UserModel
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers

interface UserApi {


    @GET("user/profile")
    @Headers("Accept: application/json")
    suspend fun getUser(@Header("Authorization") token:String?): Response<UserModel>
}