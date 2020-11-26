package com.example.smartpi.model.payment.wallet

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class LinkAjaModel(
    val data: LinkAjaData? = null,
    val user: UserLinkAja? = null
) : Parcelable

@Parcelize
data class LinkAjaData(
    val transactionDate: String? = null,
    val checkoutUrl: String? = null,
    val amount: Int? = null,
    val ewalletType: String? = null,
    val externalId: String? = null
) : Parcelable

@Parcelize
data class UserLinkAja(
    val role: String? = null,
    val flag: Int? = null,
    val activationCode: String? = null,
    val city: String? = null,
    val apiToken: String? = null,
    val createdAt: String? = null,
    val deviceCheck: Int? = null,
    val skypeId: String? = null,
    val nik: String? = null,
    val updatedAt: String? = null,
    val phone: String? = null,
    val name: String? = null,
    val id: Int? = null,
    val email: String? = null,
    val status: Int? = null
) : Parcelable
