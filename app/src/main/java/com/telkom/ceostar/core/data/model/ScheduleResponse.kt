package com.telkom.ceostar.core.data.model

import com.google.gson.annotations.SerializedName

data class ScheduleResponse(
    @SerializedName("message")
    val message: String,
    @SerializedName("data")
    val data: List<ScheduleData>
)

data class ScheduleData(
    @SerializedName("schedule_id")
    val scheduleId: Int,
    @SerializedName("train")
    val train: TrainInfo,
    @SerializedName("route")
    val route: RouteInfo,
    @SerializedName("timing")
    val timing: TimingInfo,
    @SerializedName("seat_classes")
    val seatClasses: SeatClasses
)

data class TrainInfo(
    @SerializedName("train_id")
    val trainId: Int,
    @SerializedName("train_name")
    val trainName: String,
    @SerializedName("train_code")
    val trainCode: String,
    @SerializedName("category")
    val category: String
)

data class RouteInfo(
    @SerializedName("origin_station")
    val originStation: String,
    @SerializedName("destination_station")
    val destinationStation: String,
    @SerializedName("distance")
    val distance: Int
)

data class TimingInfo(
    @SerializedName("schedule_date")
    val scheduleDate: String,
    @SerializedName("departure_time")
    val departureTime: String,
    @SerializedName("arrival_time")
    val arrivalTime: String
)

data class SeatClasses(
    @SerializedName("Bisnis")
    val bisnis: Int,
    @SerializedName("Ekonomi")
    val ekonomi: Int,
    @SerializedName("Eksekutif")
    val eksekutif: Int
)