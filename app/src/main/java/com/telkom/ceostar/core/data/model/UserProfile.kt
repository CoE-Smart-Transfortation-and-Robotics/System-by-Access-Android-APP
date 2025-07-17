package com.telkom.ceostar.core.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class UserProfile(
    val id: Int,
    val name: String,
    val email: String,
    val nik: String?,
    val phone: String?,
    val address: String?
) : Parcelable