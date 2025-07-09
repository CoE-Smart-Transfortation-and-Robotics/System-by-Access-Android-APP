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

    @POST("/api/auth/login")
    suspend fun login(@Body loginRequest: LoginRequest): Response<AuthResponse>

    @POST("/api/auth/register")
    suspend fun register(@Body registerRequest: RegisterRequest): Response<AuthResponse>

    @POST("auth/logout")
    suspend fun logout(): Response<ResponseBody>

    @GET("/")
    suspend fun pingServer(): Response<ResponseBody>

}