package com.telkom.chat.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.telkom.core.data.local.PreferencesManager
import com.telkom.core.data.repository.AuthRepository
import com.telkom.core.data.repository.ChatRepository
import com.telkom.core.di.NetworkModule
import com.telkom.core.utils.SessionManager

class ChatViewModelFactory(
    private val context: Context,
    private val targetUserId: Int? = null // Parameter untuk admin chat dengan user tertentu
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val apiService = NetworkModule.createApiService(context)
        val preferencesManager = PreferencesManager(context)
        val sessionManager = SessionManager(context)

        return when {
            modelClass.isAssignableFrom(ChatViewModel::class.java) -> {
                val chatRepository = ChatRepository(apiService)
                ChatViewModel(chatRepository, sessionManager, targetUserId) as T
            }

            modelClass.isAssignableFrom(TestConnection::class.java) -> {
                val authRepository = AuthRepository(apiService, preferencesManager)
                TestConnection(authRepository) as T
            }

            else -> throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
        }
    }
}