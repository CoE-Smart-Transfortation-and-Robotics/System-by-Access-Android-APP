package com.telkom.chat.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.telkom.core.data.model.ChatMessage
import com.telkom.core.data.repository.ChatRepository
import com.telkom.core.utils.Resource
import com.telkom.core.utils.SessionManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class ChatViewModel(
    private val chatRepository: ChatRepository,
    private val sessionManager: SessionManager,
    private val targetUserId: Int? = null // Parameter untuk admin chat dengan user tertentu
) : ViewModel() {

    private val _messagesState = MutableStateFlow<Resource<List<ChatMessage>>?>(null)
    val messagesState: StateFlow<Resource<List<ChatMessage>>?> = _messagesState.asStateFlow()

    private val _sendMessageState = MutableStateFlow<Resource<ChatMessage>?>(null)
    val sendMessageState: StateFlow<Resource<ChatMessage>?> = _sendMessageState.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    // Menentukan target chat
    private fun getChatTargetId(): Int {
        return targetUserId ?: 1 // Jika null (user), chat ke admin (ID=1). Jika ada value (admin), chat ke user tersebut
    }

    fun loadChatMessages() {
        viewModelScope.launch {
            val chatTargetId = getChatTargetId()
            chatRepository.getChatMessages(chatTargetId).collect { resource ->
                _messagesState.value = resource
                _isLoading.value = resource is Resource.Loading
            }
        }
    }

    fun sendMessage(message: String) {
        if (message.trim().isEmpty()) return

        viewModelScope.launch {
            val chatTargetId = getChatTargetId()
            chatRepository.sendMessage(chatTargetId, message.trim()).collect { resource ->
                _sendMessageState.value = resource

                when (resource) {
                    is Resource.Success -> {
                        loadChatMessages()
                    }
                    is Resource.Error -> {
                        // Handle error di UI
                    }
                    is Resource.Loading -> {
                        // Show loading state
                    }
                }
            }
        }
    }

    fun refreshMessages() {
        loadChatMessages()
    }

    fun getCurrentUserId(): Int {
        return sessionManager.fetchUserId() ?: 0
    }

    fun getTargetUserId(): Int? {
        return targetUserId
    }

    fun clearSendMessageState() {
        _sendMessageState.value = null
    }

    fun formatTimestamp(timestamp: String?): String {
        return try {
            // Coba parse sebagai ISO 8601 dulu
            val isoFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
            isoFormat.timeZone = TimeZone.getTimeZone("UTC")

            val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())

            timestamp?.let {
                val date = isoFormat.parse(it)
                timeFormat.format(date ?: Date())
            } ?: timeFormat.format(Date())
        } catch (e: Exception) {
            // Fallback: coba parse sebagai long timestamp
            try {
                val timestampLong = timestamp?.toLongOrNull() ?: System.currentTimeMillis()
                SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date(timestampLong))
            } catch (e2: Exception) {
                SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date())
            }
        }
    }

}