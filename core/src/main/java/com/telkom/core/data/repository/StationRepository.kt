package com.telkom.core.data.repository

import com.telkom.core.data.model.Station
import com.telkom.core.network.ApiResponse
import com.telkom.core.network.ApiService
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class StationRepository @Inject constructor(private val apiService: ApiService) {

    suspend fun getAllStations(): ApiResponse<List<Station>> {
        return try {
            val response = apiService.getAllStations()
            if (response.isSuccessful) {
                ApiResponse(
                    success = true,
                    message = "Success",
                    data = response.body()
                )
            } else {
                ApiResponse(
                    success = false,
                    message = "Error: ${response.code()}",
                    data = null
                )
            }
        } catch (e: Exception) {
            ApiResponse(
                success = false,
                message = e.message.toString(),
                data = null
            )
        }
    }
}