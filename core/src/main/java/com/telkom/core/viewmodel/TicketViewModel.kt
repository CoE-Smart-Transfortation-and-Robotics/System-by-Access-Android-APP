package com.telkom.core.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.telkom.core.data.model.BookingTicket
import com.telkom.core.data.repository.TicketRepository
import com.telkom.core.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

@HiltViewModel
class TicketViewModel @Inject constructor(
    private val ticketRepository: TicketRepository
) : ViewModel() {

    private val _ticketsState = MutableStateFlow<Resource<List<BookingTicket>>>(Resource.Loading())
    val ticketsState: StateFlow<Resource<List<BookingTicket>>> = _ticketsState.asStateFlow()

    fun loadMyTickets() {
        viewModelScope.launch {
            ticketRepository.getMyTickets().collect { resource ->
                when (resource) {
                    is Resource.Success -> {
                        val filteredTickets = filterActiveTickets(resource.data ?: emptyList())
                        _ticketsState.value = Resource.Success(filteredTickets)
                    }
                    is Resource.Error -> {
                        _ticketsState.value = resource
                    }
                    is Resource.Loading -> {
                        _ticketsState.value = resource
                    }
                }
            }
        }
    }

    private fun filterActiveTickets(tickets: List<BookingTicket>): List<BookingTicket> {
        val today = Calendar.getInstance()
        today.set(Calendar.HOUR_OF_DAY, 0)
        today.set(Calendar.MINUTE, 0)
        today.set(Calendar.SECOND, 0)
        today.set(Calendar.MILLISECOND, 0)

        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

        return tickets.filter { ticket ->
            try {
                val ticketDate = dateFormat.parse(ticket.trainSchedule.scheduleDate)
                val ticketCalendar = Calendar.getInstance().apply {
                    time = ticketDate!!
                    set(Calendar.HOUR_OF_DAY, 0)
                    set(Calendar.MINUTE, 0)
                    set(Calendar.SECOND, 0)
                    set(Calendar.MILLISECOND, 0)
                }

                // Tampilkan tiket jika tanggalnya hari ini atau masa depan
                !ticketCalendar.before(today)
            } catch (e: Exception) {
                // Jika parsing gagal, tetap tampilkan tiket
                true
            }
        }
    }
}