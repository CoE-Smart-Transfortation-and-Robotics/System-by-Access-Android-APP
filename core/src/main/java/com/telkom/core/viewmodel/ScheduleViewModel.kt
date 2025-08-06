package com.telkom.core.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.telkom.core.data.model.ScheduleResponse
import com.telkom.core.data.repository.TrainRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ScheduleViewModel @Inject constructor(
    private val trainRepository: TrainRepository
) : ViewModel() {

    private val _schedules = MutableLiveData<ScheduleResponse>()
    val schedules: LiveData<ScheduleResponse> = _schedules

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> = _errorMessage

    fun fetchTrainSchedules(
        originStationId: Int,
        destinationStationId: Int,
        scheduleDate: String,
        trainType: Int // Tambahkan parameter ini
    ) {
        _isLoading.value = true
        viewModelScope.launch {
            try {
                val response = trainRepository.getTrainSchedules(
                    originStationId,
                    destinationStationId,
                    scheduleDate,
                    trainType // Teruskan parameter
                )
                if (response.isSuccessful) {
                    _schedules.postValue(response.body())
                } else {
                    _errorMessage.postValue("Error: ${response.message()}")
                }
            } catch (e: Exception) {
                _errorMessage.postValue("Failure: ${e.message}")
            } finally {
                _isLoading.postValue(false)
            }
        }
    }
}