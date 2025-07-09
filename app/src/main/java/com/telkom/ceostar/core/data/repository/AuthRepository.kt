package com.telkom.ceostar.core.data.repository

import com.telkom.ceostar.core.data.local.PreferencesManager
import com.telkom.ceostar.core.data.model.AuthData
import com.telkom.ceostar.core.data.model.LoginRequest
import com.telkom.ceostar.core.data.model.RegisterRequest
import com.telkom.ceostar.core.data.model.User
import com.telkom.ceostar.core.network.ApiService
import com.telkom.ceostar.core.utils.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepository @Inject constructor(
    private val apiService: ApiService,
    private val preferencesManager: PreferencesManager
) {

    fun login(email: String, password: String): Flow<Resource<AuthData>> = flow {
        emit(Resource.Loading())
        try {
            val response = apiService.login(LoginRequest(email, password))
            if (response.isSuccessful) {
                val authResponse = response.body()

                if (authResponse?.success == true &&
                    authResponse.token != null &&
                    authResponse.user != null) {

                    // Create AuthData from response
                    val authData = AuthData(
                        token = authResponse.token,
                        user = authResponse.user
                    )

                    // Save auth data to preferences
                    preferencesManager.saveAuthData(
                        authData.token,
                        authData.user
                    )

                    emit(Resource.Success(authData))
                } else {
                    emit(Resource.Error(authResponse?.message ?: "Login failed"))
                }
            } else {
                emit(Resource.Error("Network error: ${response.code()}"))
            }
        } catch (e: Exception) {
            emit(Resource.Error(e.localizedMessage ?: "An unexpected error occurred"))
        }
    }

    fun register(
        name: String,
        email: String,
        password: String,
        confirmPassword: String
    ): Flow<Resource<AuthData>> = flow {
        emit(Resource.Loading())
        try {
            val response = apiService.register(
                RegisterRequest(name, email, password, confirmPassword)
            )
            if (response.isSuccessful) {
                val authResponse = response.body()

                if (authResponse?.success == true &&
                    authResponse.token != null &&
                    authResponse.user != null) {

                    // Create AuthData from response
                    val authData = AuthData(
                        token = authResponse.token,
                        user = authResponse.user
                    )

                    // Save auth data to preferences
                    preferencesManager.saveAuthData(
                        authData.token,
                        authData.user
                    )

                    emit(Resource.Success(authData))
                } else {
                    emit(Resource.Error(authResponse?.message ?: "Registration failed"))
                }
            } else {
                emit(Resource.Error("Network error: ${response.code()}"))
            }
        } catch (e: Exception) {
            emit(Resource.Error(e.localizedMessage ?: "An unexpected error occurred"))
        }
    }

    suspend fun logout(): Flow<Resource<String>> = flow {
        emit(Resource.Loading())
        try {
            val response = apiService.logout()
            if (response.isSuccessful) {
                preferencesManager.clearAuthData()
                emit(Resource.Success("Logout successful"))
            } else {
                emit(Resource.Error("Logout failed"))
            }
        } catch (e: Exception) {
            // Clear local data even if network call fails
            preferencesManager.clearAuthData()
            emit(Resource.Success("Logout successful"))
        }
    }

    fun testConnection(): Flow<Resource<String>> = flow {
        emit(Resource.Loading())
        try {
            val response = apiService.pingServer()
            if (response.isSuccessful) {
                emit(Resource.Success("Koneksi berhasil ke server"))
            } else {
                emit(Resource.Error("Server merespons dengan kode: ${response.code()}"))
            }
        } catch (e: java.net.UnknownHostException) {
            emit(Resource.Error("Tidak bisa terhubung ke server. Periksa URL atau koneksi internet"))
        } catch (e: java.net.SocketTimeoutException) {
            emit(Resource.Error("Timeout - Server tidak merespons"))
        } catch (e: Exception) {
            emit(Resource.Error("Error: ${e.localizedMessage}"))
        }
    }

    fun getAuthToken(): Flow<String?> = preferencesManager.getAuthToken()

    fun getUserData(): Flow<User?> = preferencesManager.getUserData()
}