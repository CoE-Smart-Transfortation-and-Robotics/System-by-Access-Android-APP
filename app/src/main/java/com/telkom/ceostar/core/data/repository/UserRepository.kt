package com.telkom.ceostar.core.data.repository

import com.telkom.ceostar.core.data.model.UpdateProfileRequest
import com.telkom.ceostar.core.data.model.UserProfile
import com.telkom.ceostar.core.data.model.UserResponse
import com.telkom.ceostar.core.network.ApiService
import com.telkom.ceostar.core.utils.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import kotlin.String

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

    fun updateProfile(
        id: Int,
        name: String,
        email: String,
        password: String?,
        confirmPassword: String?,
        nik: String?,
        phone: String?,
        address: String?
    ): Flow<Resource<UserResponse>> = flow {
        emit(Resource.Loading())
        try {
            val request = UpdateProfileRequest(name, email, password, confirmPassword, nik, phone, address)
            val response = apiService.updateProfile(id, request)
            if (response.isSuccessful) {
                response.body()?.let { userResponse ->
                    emit(Resource.Success(userResponse))
                } ?: emit(Resource.Error("Update response body is null"))
            } else {
                emit(Resource.Error("Network error: ${response.code()}"))
            }
        } catch (e: Exception) {
            emit(Resource.Error(e.localizedMessage ?: "An unexpected error occurred"))
        }
    }
}