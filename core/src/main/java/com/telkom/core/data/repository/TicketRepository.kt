package com.telkom.core.data.repository

import com.telkom.core.data.model.BookingTicket
import com.telkom.core.network.ApiService
import com.telkom.core.utils.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TicketRepository @Inject constructor(
    private val apiService: ApiService
) {
    fun getMyTickets(): Flow<Resource<List<BookingTicket>>> = flow {
        try {
            emit(Resource.Loading())

            val response = apiService.getMyBookings()

            if (response.isSuccessful) {
                response.body()?.let { tickets ->
                    emit(Resource.Success(tickets))
                } ?: emit(Resource.Error("No tickets data found"))
            } else {
                emit(Resource.Error("Failed to fetch tickets: ${response.message()}"))
            }
        } catch (e: Exception) {
            emit(Resource.Error("Network error: ${e.localizedMessage}"))
        }
    }.flowOn(Dispatchers.IO)
}