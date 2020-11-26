package com.example.smartpi.model.payment.wallet

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class GopayModel(

    @field:SerializedName("data")
    val data: GopayData? = null
) : Parcelable

@Parcelize
data class ActionsItem(

    @field:SerializedName("method")
    val method: String? = null,

    @field:SerializedName("name")
    val name: String? = null,

    @field:SerializedName("url")
    val url: String? = null
) : Parcelable

@Parcelize
data class GopayData(

    @field:SerializedName("status_message")
    val statusMessage: String? = null,

    @field:SerializedName("transaction_id")
    val transactionId: String? = null,

    @field:SerializedName("fraud_status")
    val fraudStatus: String? = null,

    @field:SerializedName("payment_type")
    val paymentType: String? = null,

    @field:SerializedName("transaction_status")
    val transactionStatus: String? = null,

    @field:SerializedName("status_code")
    val statusCode: String? = null,

    @field:SerializedName("transaction_time")
    val transactionTime: String? = null,

    @field:SerializedName("currency")
    val currency: String? = null,

    @field:SerializedName("merchant_id")
    val merchantId: String? = null,

    @field:SerializedName("gross_amount")
    val grossAmount: String? = null,

    @field:SerializedName("order_id")
    val orderId: String? = null,

    @field:SerializedName("actions")
    val actions: List<ActionsItem?>? = null
) : Parcelable
