package com.telkom.ceostar.core.repository

import com.telkom.ceostar.core.data.model.Seat
import com.telkom.ceostar.core.network.ApiService
import com.telkom.ceostar.core.utils.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SeatRepository @Inject constructor(
    private val apiService: ApiService
) {
    fun getAvailableSeats(
        trainId: Int,
        scheduleDate: String,
        originStationId: Int,
        destinationStationId: Int
    ): Flow<Resource<List<Seat>>> = flow {
        try {
            emit(Resource.Loading())

            val response = apiService.getAvailableSeats(
                trainId = trainId,
                scheduleDate = scheduleDate,
                originStationId = originStationId,
                destinationStationId = destinationStationId
            )

            if (response.isSuccessful) {
                response.body()?.let { seats ->
                    emit(Resource.Success(seats))
                } ?: emit(Resource.Error("No seats data found"))
            } else {
                emit(Resource.Error("Failed to fetch seats: ${response.message()}"))
            }
        } catch (e: Exception) {
            emit(Resource.Error("Network error: ${e.localizedMessage}"))
        }
    }.flowOn(Dispatchers.IO)
}