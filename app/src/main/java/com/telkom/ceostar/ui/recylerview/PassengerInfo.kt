package com.telkom.ceostar.ui.recylerview

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class PassengerInfo(
    var nik: String = "",
    var name: String = ""
) : Parcelable