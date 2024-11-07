package com.cnpm.bikerentalapp.exception.model

import java.time.LocalDateTime

data class ErrorResponse(
    val timestamp: LocalDateTime,
    val status: Int,
    val error: String,
    val type: Any,
    val message: String,
    val path: String,
)
