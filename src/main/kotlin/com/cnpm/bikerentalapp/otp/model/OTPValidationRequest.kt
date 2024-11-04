package com.cnpm.bikerentalapp.otp.model

data class OTPValidationRequest (
    val otp: String,
    val username: String
)