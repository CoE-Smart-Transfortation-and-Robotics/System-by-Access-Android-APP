package com.telkom.ceostar.core.network

import com.telkom.ceostar.core.data.model.AuthResponse
import com.telkom.ceostar.core.data.model.LoginRequest
import com.telkom.ceostar.core.data.model.RegisterRequest
import com.telkom.ceostar.core.data.model.Station
import com.telkom.ceostar.core.data.model.Trains
import com.telkom.ceostar.core.data.model.UpdateProfileRequest
import com.telkom.ceostar.core.data.model.UserProfile
import com.telkom.ceostar.core.data.model.UserResponse
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.HEAD
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface ApiService {

    @POST("/api/auth/login")
    suspend fun login(@Body loginRequest: LoginRequest): Response<AuthResponse>

    @POST("/api/auth/register")
    suspend fun register(@Body registerRequest: RegisterRequest): Response<AuthResponse>

    @POST("api/auth/logout")
    suspend fun logout(): Response<ResponseBody>

    @GET("/")
    suspend fun pingServer(): Response<ResponseBody>

    @GET("/api/users/profile/me")
    suspend fun getProfile(): Response<UserProfile>

    @PUT("/api/users/{id}")
    suspend fun updateProfile(@Path("id") id: Int, @Body updateProfileRequest: UpdateProfileRequest): Response<UserResponse>

    @GET("/api/stations")
    suspend fun getAllStations(): Response<List<Station>>

    @GET("/api/trains")
    suspend fun getAllTrains(): Response<Trains>

}