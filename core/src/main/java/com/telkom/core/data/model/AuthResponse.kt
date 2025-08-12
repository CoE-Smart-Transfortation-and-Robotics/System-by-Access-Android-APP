package com.telkom.core.data.model

data class AuthResponse(
    val success: Boolean,
    val message: String,
    val token: String?,
    val user: User?
)

data class AuthData(
    val token: String,
    val user: User
)