package com.telkom.core.data.model

data class BookingRequest(
    val train_id: Int,
    val schedule_date: String,
    val origin_station_id: Int,
    val destination_station_id: Int,
    val passengers: List<PassengerBooking>
)

data class PassengerBooking(
    val seat_id: Int,
    val name: String,
    val nik: String
)

data class BookingResponse(
    val success: Boolean,
    val message: String,
    val booking_id: Int? = null
)