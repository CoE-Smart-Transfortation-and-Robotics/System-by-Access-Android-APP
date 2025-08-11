package com.telkom.core.data.repository

import com.telkom.core.data.model.ChatItem
import com.telkom.core.data.model.ChatMessage
import com.telkom.core.data.model.SendMessageRequest
import com.telkom.core.network.ApiService
import com.telkom.core.utils.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.net.SocketTimeoutException
import java.net.UnknownHostException

class ChatRepository(
    private val apiService: ApiService
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

    // Get chat messages dengan user/admin tertentu
    fun getChatMessages(withUserId: Int): Flow<Resource<List<ChatMessage>>> = flow {
        emit(Resource.Loading())
        try {
            val response = apiService.getChatWithUser(withUserId)
            if (response.isSuccessful) {
                val messages = response.body() ?: emptyList()
                emit(Resource.Success(messages))
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

    // Send message ke user/admin
    fun sendMessage(toUserId: Int, message: String): Flow<Resource<ChatMessage>> = flow {
        emit(Resource.Loading())
        try {
            val request = SendMessageRequest(
                receiver_id = toUserId,
                message = message
            )
            val response = apiService.sendMessage(request)
            if (response.isSuccessful) {
                response.body()?.let { sentMessage ->
                    emit(Resource.Success(sentMessage))
                } ?: emit(Resource.Error("Response body is null"))
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
}