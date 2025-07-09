package com.telkom.ceostar.core.network

data class ApiResponse<T>(
    val success: Boolean,
    val message: String,
    val data: T? = null,
    val errors: List<String>? = null
)