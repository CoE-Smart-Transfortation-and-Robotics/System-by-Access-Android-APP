package com.telkom.ceostar.core.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.telkom.ceostar.core.data.model.BookingTicket
import com.telkom.ceostar.core.repository.TicketRepository
import com.telkom.ceostar.core.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
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
                _ticketsState.value = resource
            }
        }
    }
}