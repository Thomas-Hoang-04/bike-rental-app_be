package com.cnpm.bikerentalapp.otp.model

import com.fasterxml.jackson.annotation.JsonProperty

data class OTPRequest(
    val username: String,
    @JsonProperty("phone_number")
    val phoneNumber: String,
    val purpose: OTPPurpose
)
