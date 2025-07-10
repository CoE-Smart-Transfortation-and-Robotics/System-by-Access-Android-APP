package com.telkom.ceostar.core.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.telkom.ceostar.core.data.model.UserProfile
import com.telkom.ceostar.core.data.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
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

//    fun getProfile() {
//        viewModelScope.launch {
//            _isLoading.value = true
//            userRepository.getProfile()
//                .onSuccess {
//                    android.util.Log.d("UserViewModel", "Profile success: $it")
//                    _profile.value = it
//                }
//                .onFailure {
//                    android.util.Log.e("UserViewModel", "Profile error: ${it.message}")
//                }
//            _isLoading.value = false
//        }
//    }

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
}