package com.example.smartpi.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize


@Parcelize
data class DetailPaketModel(

    @field:SerializedName("data")
    val data: DetailPaketItem? = null
) : Parcelable

@Parcelize
data class DetailPaketItem(

    @field:SerializedName("img")
    val img: String? = null,

    @field:SerializedName("discount_info")
    val discountInfo: String? = null,

    @field:SerializedName("number_of_session")
    val numberOfSession: Int? = null,

    @field:SerializedName("created_at")
    val createdAt: String? = null,

    @field:SerializedName("duration")
    val duration: Int? = null,

    @field:SerializedName("updated_at")
    val updatedAt: String? = null,

    @field:SerializedName("price")
    val price: String? = null,

    @field:SerializedName("product_id")
    val productId: Int? = null,

    @field:SerializedName("name")
    val name: String? = null,

    @field:SerializedName("id")
    val id: Int? = null,

    @field:SerializedName("durasi")
    val durasi: String? = null,

    @field:SerializedName("is_promo")
    val isPromo: String? = null,

    @field:SerializedName("desc")
    val desc: String? = null
) : Parcelable
