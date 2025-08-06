package com.telkom.core.data.model

data class User(
    val id: Int,
    val name: String,
    val email: String,
    val role: String,
    val phoneNumber: String? = null,
    val createdAt: String? = null
)