package com.cnpm.bikerentalapp.user.model.dto

data class TopUpRequest(
    val from: String,
    val to: String = from,
    val amount: Int
)
