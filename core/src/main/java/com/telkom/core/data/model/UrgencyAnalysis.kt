package com.telkom.core.data.model

data class UrgencyAnalysis(
    val is_urgent: Boolean,
    val confidence: Double,
    val category: String,
    val reason: String,
    val analyzed_at: AnalyzedAt
)