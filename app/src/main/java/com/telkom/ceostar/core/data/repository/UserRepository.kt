package com.telkom.ceostar.core.data.repository

import com.telkom.ceostar.core.data.model.UserProfile
import com.telkom.ceostar.core.network.ApiService
import javax.inject.Inject

class UserRepository @Inject constructor(
    private val apiService: ApiService
) {
    suspend fun getProfile(): Result<UserProfile> {
        return try {
            val response = apiService.getProfile()
            if (response.isSuccessful) {
                response.body()?.let {
                    android.util.Log.d("UserRepository", "Profile data: $it")
                    Result.success(it)
                } ?: Result.failure(Exception("Profile data is null"))
            } else {
                android.util.Log.e("UserRepository", "Response not successful: ${response.code()}")
                Result.failure(Exception("Failed to get profile: ${response.code()}"))
            }
        } catch (e: Exception) {
            android.util.Log.e("UserRepository", "Exception: ${e.message}")
            Result.failure(e)
        }
    }
}