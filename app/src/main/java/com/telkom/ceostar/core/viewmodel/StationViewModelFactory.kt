package com.telkom.ceostar.core.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.telkom.ceostar.core.data.repository.StationRepository

class StationViewModelFactory(private val repository: StationRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(StationViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return StationViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}