package com.telkom.ceostar.ui.booking

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class PassengerData(
    val name: String = "",
    val nik: String = ""
) : Parcelable