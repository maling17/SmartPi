package com.example.smartpi.model.payment.wallet

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class OVOModel(

    @field:SerializedName("data")
    val data: OVOData? = null,

    @field:SerializedName("user")
    val user: UserOVO? = null
) : Parcelable

@Parcelize
data class OVOData(

    @field:SerializedName("amount")
    val amount: Int? = null,

    @field:SerializedName("ewallet_type")
    val ewalletType: String? = null,

    @field:SerializedName("phone")
    val phone: String? = null,

    @field:SerializedName("created")
    val created: String? = null,

    @field:SerializedName("external_id")
    val externalId: String? = null,

    @field:SerializedName("business_id")
    val businessId: String? = null,

    @field:SerializedName("status")
    val status: String? = null
) : Parcelable

@Parcelize
data class UserOVO(

    @field:SerializedName("role")
    val role: String? = null,

    @field:SerializedName("flag")
    val flag: Int? = null,

    @field:SerializedName("activation_code")
    val activationCode: String? = null,

    @field:SerializedName("city")
    val city: String? = null,

    @field:SerializedName("api_token")
    val apiToken: String? = null,

    @field:SerializedName("created_at")
    val createdAt: String? = null,

    @field:SerializedName("device_check")
    val deviceCheck: Int? = null,

    @field:SerializedName("skype_id")
    val skypeId: String? = null,

    @field:SerializedName("nik")
    val nik: String? = null,

    @field:SerializedName("updated_at")
    val updatedAt: String? = null,

    @field:SerializedName("phone")
    val phone: String? = null,

    @field:SerializedName("name")
    val name: String? = null,

    @field:SerializedName("id")
    val id: Int? = null,

    @field:SerializedName("email")
    val email: String? = null,

    @field:SerializedName("status")
    val status: Int? = null
) : Parcelable
