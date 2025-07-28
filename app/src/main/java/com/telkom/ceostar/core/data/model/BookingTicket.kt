package com.telkom.ceostar.core.data.model

import com.google.gson.annotations.SerializedName

data class BookingTicket(
    @SerializedName("id")
    val id: Int,
    @SerializedName("user_id")
    val userId: Int,
    @SerializedName("schedule_id")
    val scheduleId: Int,
    @SerializedName("origin_station_id")
    val originStationId: Int,
    @SerializedName("destination_station_id")
    val destinationStationId: Int,
    @SerializedName("status")
    val status: String,
    @SerializedName("price")
    val price: String,
    @SerializedName("booking_date")
    val bookingDate: String,
    @SerializedName("created_at")
    val createdAt: String,
    @SerializedName("updated_at")
    val updatedAt: String,
    @SerializedName("passengers")
    val passengers: List<PassengerTicket>,
    @SerializedName("TrainSchedule")
    val trainSchedule: TrainScheduleTicket,
    @SerializedName("OriginStation")
    val originStation: StationTicket,
    @SerializedName("DestinationStation")
    val destinationStation: StationTicket
)

data class PassengerTicket(
    @SerializedName("name")
    val name: String,
    @SerializedName("nik")
    val nik: String,
    @SerializedName("seat_id")
    val seatId: Int
)

data class TrainScheduleTicket(
    @SerializedName("schedule_date")
    val scheduleDate: String,
    @SerializedName("Train")
    val train: TrainTicket
)

data class TrainTicket(
    @SerializedName("train_name")
    val trainName: String,
    @SerializedName("train_code")
    val trainCode: String
)

data class StationTicket(
    @SerializedName("station_name")
    val stationName: String
)