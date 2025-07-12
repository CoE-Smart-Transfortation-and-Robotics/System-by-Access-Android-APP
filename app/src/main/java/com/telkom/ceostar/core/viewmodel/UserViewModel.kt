package com.telkom.ceostar.core.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.telkom.ceostar.core.data.model.AuthData
import com.telkom.ceostar.core.data.model.UserProfile
import com.telkom.ceostar.core.data.model.UserResponse
import com.telkom.ceostar.core.data.repository.UserRepository
import com.telkom.ceostar.core.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {

    private val _profile = MutableLiveData<UserProfile>()
    val profile: LiveData<UserProfile> = _profile

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _updateState = MutableStateFlow<Resource<UserResponse>?>(null)
    val updateState: StateFlow<Resource<UserResponse>?> = _updateState.asStateFlow()

    fun getProfile() {
        viewModelScope.launch(Dispatchers.IO) {
            _isLoading.postValue(true)
            userRepository.getProfile()
                .onSuccess {
                    android.util.Log.d("UserViewModel", "Profile success: $it")
                    _profile.postValue(it)
                }
                .onFailure {
                    android.util.Log.e("UserViewModel", "Profile error: ${it.message}")
                }
            _isLoading.postValue(false)
        }
    }

    fun updateProfile(id: Int, name: String, email: String, password: String, confirmPassword: String, nik: String?, phone: String?, address: String?) {
        viewModelScope.launch(Dispatchers.IO) {
            userRepository.updateProfile(id, name, email, password, confirmPassword, nik, phone, address).collect {
                _updateState.value = it
            }
        }
    }
}