package com.telkom.admin.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.telkom.core.data.model.ChatItem
import com.telkom.core.data.repository.ChatRepository
import com.telkom.core.utils.Resource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ChatViewModel(
    private val chatRepository: ChatRepository
) : ViewModel() {

    private val _chatListState = MutableStateFlow<Resource<List<ChatItem>>?>(null)
    val chatListState: StateFlow<Resource<List<ChatItem>>?> = _chatListState.asStateFlow()

    private val _urgentChatState = MutableStateFlow<Resource<List<ChatItem>>?>(null)
    val urgentChatState: StateFlow<Resource<List<ChatItem>>?> = _urgentChatState.asStateFlow()

    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing: StateFlow<Boolean> = _isRefreshing.asStateFlow()

    fun getAllChats() {
        viewModelScope.launch {
            chatRepository.getAllChats().collect {
                _chatListState.value = it
            }
        }
    }

    fun getUrgentChats() {
        viewModelScope.launch {
            chatRepository.getUrgentChats().collect {
                _urgentChatState.value = it
            }
        }
    }

    fun refreshChats() {
        _isRefreshing.value = true
        viewModelScope.launch {
            chatRepository.getAllChats().collect {
                _chatListState.value = it
                _isRefreshing.value = false
            }
        }
    }

    fun filterChatsByCategory(category: String) {
        val currentChats = _chatListState.value
        if (currentChats is Resource.Success) {
            val filteredChats = currentChats.data!!.filter { chat ->
                chat.urgency_analysis?.category?.contains(category, ignoreCase = true) == true
            }
            _chatListState.value = Resource.Success(filteredChats)
        }
    }
}