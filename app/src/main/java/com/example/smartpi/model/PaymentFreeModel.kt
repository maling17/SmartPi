package com.example.smartpi.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class PaymentFreeModel(

    @field:SerializedName("data")
    val data: FreeData? = null,

    @field:SerializedName("message")
    val message: String? = null
) : Parcelable

@Parcelize
data class FreeData(

    @field:SerializedName("kode_voucher")
    val kodeVoucher: String? = null,

    @field:SerializedName("payment_status")
    val paymentStatus: Int? = null,

    @field:SerializedName("level_id")
    val levelId: String? = null,

    @field:SerializedName("payment_amount")
    val paymentAmount: Int? = null,

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
    val orderId: Int? = null,

    @field:SerializedName("status")
    val status: Int? = null
) : Parcelable
