package com.telkom.ceostar.core.network

import com.telkom.ceostar.core.data.model.AuthResponse
import com.telkom.ceostar.core.data.model.LoginRequest
import com.telkom.ceostar.core.data.model.RegisterRequest
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.HEAD
import retrofit2.http.POST

interface ApiService {

    @POST("auth/login")
    suspend fun login(@Body loginRequest: LoginRequest): Response<AuthResponse>

    @POST("auth/register")
    suspend fun register(@Body registerRequest: RegisterRequest): Response<AuthResponse>

    @POST("auth/logout")
    suspend fun logout(): Response<ApiResponse<Any>>

    @GET("api/train-schedules")
    suspend fun pingServer(): Response<ResponseBody>

}