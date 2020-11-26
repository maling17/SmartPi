package com.example.smartpi.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class VoucherModel(

	@field:SerializedName("data")
	val data: VoucherData? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("status")
	val status: Boolean? = null
) : Parcelable

@Parcelize
data class VoucherData(

	@field:SerializedName("end_date")
	val endDate: String? = null,

	@field:SerializedName("updated_at")
	val updatedAt: String? = null,

	@field:SerializedName("voucher_name")
	val voucherName: String? = null,

	@field:SerializedName("kuota")
	val kuota: Int? = null,

	@field:SerializedName("created_at")
	val createdAt: String? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("voucher_value")
	val voucherValue: Int? = null,

	@field:SerializedName("package_id")
	val packageId: Int? = null,

	@field:SerializedName("paket")
	val paket: String? = null,

	@field:SerializedName("start_date")
	val startDate: String? = null
) : Parcelable
