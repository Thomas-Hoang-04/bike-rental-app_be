package com.example.cnpm.otp.model

data class OTPValidationRequest (
    val otp: String,
    val username: String
)