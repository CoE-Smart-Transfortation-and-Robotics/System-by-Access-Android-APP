package com.telkom.ceostar.core.data.model


data class UserProfile(
    val id: Int,
    val name: String,
    val email: String,
    val phone: String?,
    val address: String?
)