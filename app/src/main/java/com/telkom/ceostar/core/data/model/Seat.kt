package com.telkom.ceostar.core.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Seat(
    val seat_id: Int,
    val seat_number: String,
    val `class`: String,
    val carriage_id: Int,
    val carriage_number: Int,
    val train_name: String,
    val is_booked: Int
) : Parcelable {
    val isBooked: Boolean
        get() = is_booked == 1
}