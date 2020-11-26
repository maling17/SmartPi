package com.example.smartpi.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class XenditModel(

    @field:SerializedName("msg")
    val msg: String? = null,

    @field:SerializedName("package")
    val jsonMemberPackage: JsonMemberPackage? = null,

    @field:SerializedName("url")
    val url: String? = null,

    @field:SerializedName("status")
    val status: Boolean? = null
) : Parcelable

@Parcelize
data class JsonMemberPackage(

    @field:SerializedName("kode_voucher")
    val kodeVoucher: String? = null,

    @field:SerializedName("payment_status")
    val paymentStatus: Int? = null,

    @field:SerializedName("level_id")
    val levelId: Int? = null,

    @field:SerializedName("payment_amount")
    val paymentAmount: String? = null,

    @field:SerializedName("created_at")
    val createdAt: String? = null,

    @field:SerializedName("register_date")
    val registerDate: String? = null,

    @field:SerializedName("package_id")
    val packageId: String? = null,

    @field:SerializedName("expired_date")
    val expiredDate: String? = null,

    @field:SerializedName("updated_at")
    val updatedAt: String? = null,

    @field:SerializedName("user_id")
    val userId: Int? = null,

    @field:SerializedName("session_complete")
    val sessionComplete: Int? = null,

    @field:SerializedName("session_available")
    val sessionAvailable: Int? = null,

    @field:SerializedName("id")
    val id: Int? = null,

    @field:SerializedName("order_id")
    val orderId: String? = null,

    @field:SerializedName("status")
    val status: Int? = null
) : Parcelable
