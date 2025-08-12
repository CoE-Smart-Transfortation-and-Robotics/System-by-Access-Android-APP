package com.telkom.admin.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.telkom.core.data.repository.AuthRepository
import com.telkom.core.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class TestConnection(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _connectionState = MutableStateFlow<Resource<String>?>(null)
    val connectionState: StateFlow<Resource<String>?> = _connectionState.asStateFlow()

    fun testConnection() {
        viewModelScope.launch {
            authRepository.testConnection().collect {
                _connectionState.value = it
            }
        }
    }
}