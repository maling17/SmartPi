package com.example.smartpi.api

import com.example.smartpi.model.CountryCodeModel
import com.google.gson.annotations.SerializedName
import retrofit2.Response
import retrofit2.http.GET

interface CountyCodeApi {

    @GET("user/country-code")
    @SerializedName("data")
    suspend fun getCountryCode(): Response<CountryCodeModel>
}