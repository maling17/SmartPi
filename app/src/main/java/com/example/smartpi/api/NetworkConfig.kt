package com.example.smartpi.api

import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create


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
    fun getUser(): UserApi = getNetwork.create(UserApi::class.java)
    fun getJadwalUser(): JadwalUserApi = getNetwork.create(JadwalUserApi::class.java)
    fun getCheckTrial(): CheckTrialApi = getNetwork.create(CheckTrialApi::class.java)
    fun resetPassword(): ResetPasswordApi = getNetwork.create(ResetPasswordApi::class.java)



}