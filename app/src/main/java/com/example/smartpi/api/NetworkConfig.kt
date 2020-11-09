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
    fun getUser(): UserApi = getNetwork.create(UserApi::class.java)
    fun getJadwalUser(): JadwalUserApi = getNetwork.create(JadwalUserApi::class.java)
    fun getCheckTrial(): CheckTrialApi = getNetwork.create(CheckTrialApi::class.java)
    fun resetPassword(): LupaPasswordApi = getNetwork.create(LupaPasswordApi::class.java)
    fun cancelSchedule(): BatalScheduleApi = getNetwork.create(BatalScheduleApi::class.java)
    fun signUp(): SignUpApi = getNetwork.create(SignUpApi::class.java)
    fun getPromo(): PromoApi = getNetwork.create(PromoApi::class.java)
    fun packageTrial(): TrialApi = getNetwork.create(TrialApi::class.java)
    fun getPackageActive(): PackageApi = getNetwork.create(PackageApi::class.java)
    fun getTeacher(): TeacherApi = getNetwork.create(TeacherApi::class.java)


}