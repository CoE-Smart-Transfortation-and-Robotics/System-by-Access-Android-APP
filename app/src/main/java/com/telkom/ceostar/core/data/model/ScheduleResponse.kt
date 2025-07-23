package com.telkom.ceostar.core.data.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.Parcelize

data class ScheduleResponse(
    @SerializedName("message")
    val message: String,
    @SerializedName("data")
    val data: List<ScheduleData>
)

@Parcelize
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
    val seatClasses: SeatClasses,
    @SerializedName("pricing")
    val pricing: Pricing,
): Parcelable {
    @IgnoredOnParcel
    val onClick: (() -> Unit)? = null
}

@Parcelize
data class TrainInfo(
    @SerializedName("train_id")
    val trainId: Int,
    @SerializedName("train_name")
    val trainName: String,
    @SerializedName("train_code")
    val trainCode: String,
    @SerializedName("category")
    val category: String
) : Parcelable

@Parcelize
data class RouteInfo(
    @SerializedName("origin_station")
    val originStation: String,
    @SerializedName("destination_station")
    val destinationStation: String,
    @SerializedName("distance")
    val distance: Int
) : Parcelable

@Parcelize
data class TimingInfo(
    @SerializedName("schedule_date")
    val scheduleDate: String,
    @SerializedName("departure_time")
    val departureTime: String,
    @SerializedName("arrival_time")
    val arrivalTime: String
) : Parcelable

@Parcelize
data class SeatClasses(
    @SerializedName("Bisnis")
    val bisnis: Int,
    @SerializedName("Ekonomi")
    val ekonomi: Int,
    @SerializedName("Eksekutif")
    val eksekutif: Int
) : Parcelable

@Parcelize
data class Pricing(
    @SerializedName("Bisnis")
    val bisnis: Int,
    @SerializedName("Ekonomi")
    val ekonomi: Int,
    @SerializedName("Eksekutif")
    val eksekutif: Int
) : Parcelable