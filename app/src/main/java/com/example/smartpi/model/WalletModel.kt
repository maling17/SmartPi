package com.example.smartpi.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class WalletModel(

    @field:SerializedName("data")
    val data: List<WalletItem?>? = null
) : Parcelable

@Parcelize
data class WalletItem(

    @field:SerializedName("code")
    val code: String? = null,

    @field:SerializedName("name")
    val name: String? = null,

    @field:SerializedName("logo")
    val logo: String? = null
) : Parcelable
