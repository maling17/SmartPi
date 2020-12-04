package com.example.smartpi.api

import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit


class NetworkConfig {

    val okHttpClient = OkHttpClient().newBuilder()
        .connectTimeout(20, TimeUnit.SECONDS)
        .build()
    val productionApi = "https://api.smartpi.id/api/"
    val baseurl = "https://wiztalk.co/api/"
    var gson = GsonBuilder()
        .setLenient()
        .create()
    val getNetwork = Retrofit.Builder()
        .baseUrl(baseurl)
        .addConverterFactory(GsonConverterFactory.create(gson))
        .client(okHttpClient)
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
    fun checkSession(): CheckSessionApi = getNetwork.create(CheckSessionApi::class.java)
    fun createSchedule(): CreateScheduleApi = getNetwork.create(CreateScheduleApi::class.java)
    fun getPrograms(): ProgramApi = getNetwork.create(ProgramApi::class.java)
    fun getAfterClass(): AfterClassApi = getNetwork.create(AfterClassApi::class.java)
    fun getPaketLangganan(): PaketLanggananApi = getNetwork.create(PaketLanggananApi::class.java)
    fun getWallet(): PembayaranApi = getNetwork.create(PembayaranApi::class.java)
    fun profile(): ProfileApi = getNetwork.create(ProfileApi::class.java)
    fun syarat(): SyaratDanLainApi = getNetwork.create(SyaratDanLainApi::class.java)


}