package com.telkom.core.data.model

data class SendMessageRequest(
    val receiver_id: Int,
    val message: String
)