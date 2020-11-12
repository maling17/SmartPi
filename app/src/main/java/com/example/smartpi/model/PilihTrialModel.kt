package com.example.smartpi.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class PilihTrialModel(

    @field:SerializedName("data")
    val data: List<TrialItem?>? = null
) : Parcelable

@Parcelize
data class TrialItem(

    @field:SerializedName("id_paket")
    val idPaket: Int? = null,

    @field:SerializedName("nama_paket")
    val namaPaket: String? = null
) : Parcelable
