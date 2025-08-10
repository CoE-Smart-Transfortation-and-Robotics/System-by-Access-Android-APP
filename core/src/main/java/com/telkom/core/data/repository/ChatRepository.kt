package com.telkom.core.data.repository

import com.telkom.core.data.local.PreferencesManager
import com.telkom.core.data.model.ChatItem
import com.telkom.core.network.ApiService
import com.telkom.core.utils.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.net.SocketTimeoutException
import java.net.UnknownHostException

class ChatRepository(
    private val apiService: ApiService,
    private val preferencesManager: PreferencesManager
) {

    fun getAllChats(): Flow<Resource<List<ChatItem>>> = flow {
        emit(Resource.Loading())
        try {
            val response = apiService.getAllChat()
            if (response.isSuccessful) {
                val chatList = response.body() ?: emptyList()
                emit(Resource.Success(chatList))
            } else {
                emit(Resource.Error("Error: ${response.code()} - ${response.message()}"))
            }
        } catch (e: UnknownHostException) {
            emit(Resource.Error("Tidak bisa terhubung ke server. Periksa koneksi internet"))
        } catch (e: SocketTimeoutException) {
            emit(Resource.Error("Timeout - Server tidak merespons"))
        } catch (e: Exception) {
            emit(Resource.Error(e.localizedMessage ?: "Terjadi kesalahan tidak terduga"))
        }
    }

    fun getUrgentChats(): Flow<Resource<List<ChatItem>>> = flow {
        emit(Resource.Loading())
        try {
            val response = apiService.getAllChat()
            if (response.isSuccessful) {
                val chatList = response.body() ?: emptyList()
                val urgentChats = chatList.filter { it.is_urgent }
                emit(Resource.Success(urgentChats))
            } else {
                emit(Resource.Error("Error: ${response.code()} - ${response.message()}"))
            }
        } catch (e: Exception) {
            emit(Resource.Error(e.localizedMessage ?: "Terjadi kesalahan tidak terduga"))
        }
    }
}