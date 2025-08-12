package com.telkom.core.data.repository

import com.telkom.core.network.ApiService
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TrainRepository @Inject constructor(private val apiService: ApiService) {

    suspend fun getTrainSchedules(
        originStationId: Int,
        destinationStationId: Int,
        scheduleDate: String,
        trainType: Int
    ) = apiService.getTrainSchedules(trainType, originStationId, destinationStationId, scheduleDate)
}