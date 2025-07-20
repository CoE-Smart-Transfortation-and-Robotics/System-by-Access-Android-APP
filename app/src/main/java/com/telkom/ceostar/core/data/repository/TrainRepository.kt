package com.telkom.ceostar.core.data.repository

import com.telkom.ceostar.core.network.ApiService
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TrainRepository @Inject constructor(private val apiService: ApiService) {

    suspend fun getTrainSchedules(
        originStationId: Int,
        destinationStationId: Int,
        scheduleDate: String
    ) = apiService.getTrainSchedules(originStationId, destinationStationId, scheduleDate)
}