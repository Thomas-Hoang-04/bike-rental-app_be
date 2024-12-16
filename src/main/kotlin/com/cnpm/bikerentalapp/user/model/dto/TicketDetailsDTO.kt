package com.cnpm.bikerentalapp.user.model.dto

import com.cnpm.bikerentalapp.user.model.types.TicketStatus
import com.cnpm.bikerentalapp.user.model.types.TicketTypes
import com.fasterxml.jackson.annotation.JsonProperty

data class TicketDetailsDTO(
    val ticket: TicketTypes,
    val username: String,
    @JsonProperty("issued_at")
    val issuedAt: String,
    @JsonProperty("valid_till")
    val validTill: String,
    val status: TicketStatus
)
