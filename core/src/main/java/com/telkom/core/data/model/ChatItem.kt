package com.telkom.core.data.model

data class ChatItem(
    val id: String,
    val project_id: String,
    val sender_id: Int,
    val receiver_id: Int,
    val message: String,
    val timestamp: String,
    val participants: List<Int>,
    val priority_level: String?,
    val sender_role: String,
    val receiver_role: String,
    val urgency_analysis: UrgencyAnalysis?,
    val is_urgent: Boolean
)