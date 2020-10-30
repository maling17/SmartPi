package com.example.smartpi.api

import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class NetworkConfig {

    val baseurl = "https://wiztalk.co/api/"
    var gson = GsonBuilder()
        .setLenient()
        .create()
    val getNetwork = Retrofit.Builder()
        .baseUrl(baseurl)
        .addConverterFactory(GsonConverterFactory.create(gson))
        .build()

    fun inputNumber(): InputNumberApi = getNetwork.create(InputNumberApi::class.java)
    fun countryCode(): CountyCodeApi = getNetwork.create(CountyCodeApi::class.java)
    fun resendActivation(): ResendActivationApi = getNetwork.create(ResendActivationApi::class.java)
    fun inputActivation(): InputActivationApi = getNetwork.create(InputActivationApi::class.java)
    fun checkSignIn(): SignInApi = getNetwork.create(SignInApi::class.java)


}