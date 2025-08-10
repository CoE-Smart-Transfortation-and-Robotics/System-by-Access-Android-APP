package com.telkom.admin.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.telkom.core.data.local.PreferencesManager
import com.telkom.core.data.repository.AuthRepository
import com.telkom.core.data.repository.ChatRepository
import com.telkom.core.di.NetworkModule

class AdminViewModelFactory(
    private val context: Context
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val apiService = NetworkModule.createApiService(context)
        val preferencesManager = PreferencesManager(context)

        return when {
            modelClass.isAssignableFrom(ChatViewModel::class.java) -> {
                val chatRepository = ChatRepository(apiService, preferencesManager)
                ChatViewModel(chatRepository) as T
            }

            modelClass.isAssignableFrom(TestConnection::class.java) -> {
                val authRepository = AuthRepository(apiService, preferencesManager)
                TestConnection(authRepository) as T
            }

            else -> throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
        }
    }
}