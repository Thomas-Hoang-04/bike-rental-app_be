package com.cnpm.bikerentalapp.config.exception.model

data class ErrorResponse(
    val timestamp: String,
    val status: Int,
    val error: String,
    val type: Any,
    val message: String,
    val path: String,
)
