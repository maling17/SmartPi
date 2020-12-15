package com.example.smartpi.api

import com.example.smartpi.model.PaymentFreeModel
import com.example.smartpi.model.VoucherModel
import com.example.smartpi.model.WalletModel
import com.example.smartpi.model.XenditModel
import com.example.smartpi.model.payment.VA.VirtualAccountModel
import com.example.smartpi.model.payment.wallet.DANAModel
import com.example.smartpi.model.payment.wallet.GopayModel
import com.example.smartpi.model.payment.wallet.LinkAjaModel
import com.example.smartpi.model.payment.wallet.OVOModel
import com.google.gson.annotations.SerializedName
import retrofit2.Response
import retrofit2.http.*

interface PembayaranApi {

    @GET("xendit/getewallet")
    @Headers("Accept: application/json")
    @SerializedName("data")
    suspend fun getWallet(
    ): Response<WalletModel>

    @GET("xendit/getva")
    @Headers("Accept: application/json")
    @SerializedName("data")
    suspend fun getVA(
    ): Response<WalletModel>

    @FormUrlEncoded
    @POST("xendit/create/payment/ovo")
    @Headers("Accept: application/json")
    suspend fun requestOVO(
        @Header("Authorization") token: String?,
        @Field("package_id") user_available_id: String?,
        @Field("kode") kode: String?,
        @Field("phone") phone: String?
    ): Response<OVOModel>

    @FormUrlEncoded
    @POST("group/payment/ewallet")
    @Headers("Accept: application/json")
    suspend fun requestOVOGroup(
        @Header("Authorization") token: String?,
        @Field("class_id") user_available_id: String?,
        @Field("type") type: String?,
        @Field("kode") kode: String?,
        @Field("phone") phone: String?
    ): Response<OVOModel>

    @FormUrlEncoded
    @POST("xendit/create/payment/dana")
    @Headers("Accept: application/json")
    suspend fun requestDANA(
        @Header("Authorization") token: String?,
        @Field("package_id") user_available_id: String?,
        @Field("kode") kode: String?,
        @Field("phone") phone: String?
    ): Response<DANAModel>

    @FormUrlEncoded
    @POST("group/payment/ewallet")
    @Headers("Accept: application/json")
    suspend fun requestDanaGroup(
        @Header("Authorization") token: String?,
        @Field("class_id") user_available_id: String?,
        @Field("type") type: String?,
        @Field("kode") kode: String?,
        @Field("phone") phone: String?
    ): Response<DANAModel>


    @FormUrlEncoded
    @POST("xendit/create/payment/linkaja")
    @Headers("Accept: application/json")
    suspend fun requestLinkAja(
        @Header("Authorization") token: String?,
        @Field("package_id") user_available_id: String?,
        @Field("kode") kode: String?,
        @Field("phone") phone: String?
    ): Response<LinkAjaModel>

    @FormUrlEncoded
    @POST("group/payment/ewallet")
    @Headers("Accept: application/json")
    suspend fun requestLinkAjaGroup(
        @Header("Authorization") token: String?,
        @Field("class_id") user_available_id: String?,
        @Field("type") type: String?,
        @Field("kode") kode: String?,
        @Field("phone") phone: String?
    ): Response<LinkAjaModel>

    @FormUrlEncoded
    @POST("midtrans/create/payment/gopay")
    @Headers("Accept: application/json")
    suspend fun requestGopay(
        @Header("Authorization") token: String?,
        @Field("package_id") user_available_id: String?,
        @Field("kode") kode: String?,
    ): Response<GopayModel>

    @FormUrlEncoded
    @POST("group/payment/ewallet")
    @Headers("Accept: application/json")
    suspend fun requestGopayGroup(
        @Header("Authorization") token: String?,
        @Field("class_id") user_available_id: String?,
        @Field("type") type: String?,
        @Field("kode") kode: String?,
    ): Response<GopayModel>

    @FormUrlEncoded
    @POST("xendit/create/payment/va")
    @Headers("Accept: application/json")
    suspend fun requestVA(
        @Header("Authorization") token: String?,
        @Field("package_id") package_id: String?,
        @Field("bank") bank: String?,
        @Field("kode") kode: String?
    ): Response<VirtualAccountModel>

    @FormUrlEncoded
    @POST("group/payment/va")
    @Headers("Accept: application/json")
    suspend fun requestVAGroupClass(
        @Header("Authorization") token: String?,
        @Field("class_id") package_id: String?,
        @Field("bank") bank: String?,
        @Field("kode") kode: String?
    ): Response<VirtualAccountModel>

    @FormUrlEncoded
    @POST("createinvoice")
    @Headers("Accept: application/json")
    suspend fun requestXendit(
        @Header("Authorization") token: String?,
        @Field("package_id") package_id: String?,
        @Field("kode") kode: String?
    ): Response<XenditModel>

    @FormUrlEncoded
    @POST("vocher/select")
    @Headers("Accept: application/json")
    suspend fun checkVoucher(
        @Header("Authorization") token: String?,
        @Field("code") kode: String?,
        @Field("package_id") package_id: String?
    ): Response<VoucherModel>


    @FormUrlEncoded
    @POST("payment/0")
    @Headers(
        "Accept: application/json"
    )
    suspend fun paymentFree(
        @Header("Authorization") token: String?,
        @Field("code_vocher") code_voucher: String?,
        @Field("package_id") package_id: String?
    ): Response<PaymentFreeModel>
}