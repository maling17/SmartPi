package com.example.smartpi.model.payment.VA

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class VirtualAccountModel(

    @field:SerializedName("data")
    val data: VaData? = null
) : Parcelable

@Parcelize
data class VaData(

    @field:SerializedName("bank_code")
    val bankCode: String? = null,

    @field:SerializedName("account_number")
    val accountNumber: String? = null,

    @field:SerializedName("merchant_code")
    val merchantCode: String? = null,

    @field:SerializedName("owner_id")
    val ownerId: String? = null,

    @field:SerializedName("is_single_use")
    val isSingleUse: Boolean? = null,

    @field:SerializedName("external_id")
    val externalId: String? = null,

    @field:SerializedName("expiration_date")
    val expirationDate: String? = null,

    @field:SerializedName("expected_amount")
    val expectedAmount: Int? = null,

    @field:SerializedName("name")
    val name: String? = null,

    @field:SerializedName("currency")
    val currency: String? = null,

    @field:SerializedName("id")
    val id: String? = null,

    @field:SerializedName("is_closed")
    val isClosed: Boolean? = null,

    @field:SerializedName("status")
    val status: String? = null
) : Parcelable
