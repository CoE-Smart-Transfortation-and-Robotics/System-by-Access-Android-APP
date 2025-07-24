package com.telkom.ceostar.core.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.telkom.ceostar.core.data.model.Seat
import com.telkom.ceostar.core.repository.SeatRepository
import com.telkom.ceostar.core.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SeatViewModel @Inject constructor(
    private val seatRepository: SeatRepository
) : ViewModel() {

    private val _seats = MutableLiveData<List<Seat>>()
    val seats: LiveData<List<Seat>> = _seats

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> = _errorMessage

    fun getAvailableSeats(
        trainId: Int,
        scheduleDate: String,
        originStationId: Int,
        destinationStationId: Int
    ) {
        viewModelScope.launch {
            seatRepository.getAvailableSeats(
                trainId = trainId,
                scheduleDate = scheduleDate,
                originStationId = originStationId,
                destinationStationId = destinationStationId
            ).collect { resource ->
                when (resource) {
                    is Resource.Loading -> {
                        _isLoading.value = true
                    }
                    is Resource.Success -> {
                        _isLoading.value = false
                        _seats.value = resource.data ?: emptyList()
                    }
                    is Resource.Error -> {
                        _isLoading.value = false
                        _errorMessage.value = resource.message ?: "Unknown error occurred"
                    }
                }
            }
        }
    }

    fun getSeatsByClass(trainClass: String): List<Seat> {
        return _seats.value?.filter { it.`class`.equals(trainClass, ignoreCase = true) } ?: emptyList()
    }
}