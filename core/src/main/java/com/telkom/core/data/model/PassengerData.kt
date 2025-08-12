package com.telkom.core.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class PassengerData(
    val name: String = "",
    val nik: String = ""
) : Parcelable