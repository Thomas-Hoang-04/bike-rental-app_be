package com.cnpm.bikerentalapp.user.model.dto

import com.cnpm.bikerentalapp.user.model.types.TransactionPurpose
import com.cnpm.bikerentalapp.user.model.types.TransactionStatus
import com.fasterxml.jackson.annotation.JsonProperty
import java.util.*

data class TransactionsDetailsDTO(
    val id: UUID,
    val username: String,
    @JsonProperty("created_at")
    val createdAt: String,
    val amount: Int,
    val purpose: TransactionPurpose,
    val descriptions: String,
    val status: TransactionStatus
)
