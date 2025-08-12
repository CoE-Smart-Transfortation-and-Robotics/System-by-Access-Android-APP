package com.telkom.core.data.model

data class UpdateProfileRequest(
    val name: String,
    val email: String,
    val password: String?,
    val confirmPassword: String?,
    val nik: String?,
    val phone: String?,
    val address: String?
)