package com.example.smartpi.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class FilterUmum(

    var range_start: String? = null,
    var range_end: String? = null,
    var hari: String? = null,
    var kode_teacher: String? = null,
    var time_zone: String? = null,

    ) : Parcelable