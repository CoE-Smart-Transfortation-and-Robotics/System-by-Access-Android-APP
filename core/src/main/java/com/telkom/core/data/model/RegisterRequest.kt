package com.telkom.core.data.model

data class RegisterRequest(
    val name: String,
    val email: String,
    val password: String,
    val confirmPassword: String
)