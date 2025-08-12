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
            _chatListState.value = Resource.Loading()
            chatRepository.getAllChats().collect { resource ->
                when (resource) {
                    is Resource.Success -> {
                        val chatList = resource.data ?: emptyList<ChatItem>()

                        // Filter out chat dari admin sendiri
                        val filteredList = chatList.filter { chatItem ->
                            chatItem.sender_role != "admin"
                        }

                        // Sort berdasarkan urgency dulu, lalu timestamp
                        val sortedList = filteredList.sortedWith(
                            compareByDescending<ChatItem> { it.is_urgent }
                                .thenByDescending { it.timestamp }
                        )

                        _chatListState.value = Resource.Success(sortedList)
                    }
                    is Resource.Error -> {
                        _chatListState.value = resource
                    }
                    is Resource.Loading -> {
                        _chatListState.value = resource
                    }
                }
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
            chatRepository.getAllChats().collect { resource ->
                when (resource) {
                    is Resource.Success -> {
                        val chatList = resource.data ?: emptyList<ChatItem>()

                        // Filter out chat dari admin sendiri
                        val filteredList = chatList.filter { chatItem ->
                            chatItem.sender_role != "admin"
                        }

                        _chatListState.value = Resource.Success(filteredList)
                    }
                    is Resource.Error -> {
                        _chatListState.value = resource
                    }
                    is Resource.Loading -> {
                        _chatListState.value = resource
                    }
                }
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