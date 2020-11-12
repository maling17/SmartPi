package com.example.smartpi.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class PackageActiveModel(
    val data: List<PackageActiveItem?>? = null
) : Parcelable

@Parcelize
data class PackageActiveItem(
    val img: String? = null,
    val level_id: String? = null,
    val created_at: String? = null,
    val expired_date: String? = null,
    val on_going: Int? = null,
    val kode_teacher: String? = null,
    val updated_at: String? = null,
    val user_id: Int? = null,
    val session_complete: Int? = null,
    val product_id: Int? = null,
    val package_name: String? = null,
    val session_available: Int? = null,
    val id: Int? = null
) : Parcelable
