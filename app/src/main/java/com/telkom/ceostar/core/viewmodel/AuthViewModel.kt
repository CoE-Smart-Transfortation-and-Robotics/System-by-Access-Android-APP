package com.telkom.ceostar.core.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.telkom.ceostar.core.data.model.AuthData
import com.telkom.ceostar.core.data.repository.AuthRepository
import com.telkom.ceostar.core.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
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

    fun resetConnectionState() {
        _connectionState.value = null
    }

    private val _loginState = MutableStateFlow<Resource<AuthData>?>(null)
    val loginState: StateFlow<Resource<AuthData>?> = _loginState.asStateFlow()

    private val _registerState = MutableStateFlow<Resource<AuthData>?>(null)
    val registerState: StateFlow<Resource<AuthData>?> = _registerState.asStateFlow()

    fun login(email: String, password: String) {
        viewModelScope.launch {
            authRepository.login(email, password).collect {
                _loginState.value = it
            }
        }
    }

    fun register(name: String, email: String, password: String, confirmPassword: String, phoneNumber: String? = null) {
        viewModelScope.launch {
            authRepository.register(name, email, password, confirmPassword, phoneNumber).collect {
                _registerState.value = it
            }
        }
    }

    fun resetLoginState() {
        _loginState.value = null
    }

    fun resetRegisterState() {
        _registerState.value = null
    }
}