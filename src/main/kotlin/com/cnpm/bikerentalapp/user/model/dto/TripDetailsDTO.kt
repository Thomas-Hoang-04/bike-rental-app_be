package com.cnpm.bikerentalapp.user.model.dto

import com.cnpm.bikerentalapp.user.model.types.TicketTypes
import com.fasterxml.jackson.annotation.JsonProperty

data class TripDetailsDTO(
    val id: String,
    val username: String,

    @JsonProperty("bike_plate")
    val bikePlate: String,

    @JsonProperty("start_time")
    val startTime: String,

    @JsonProperty("end_time")
    val endTime: String,

    @JsonProperty("start_address")
    val startAddress: String,

    @JsonProperty("end_address")
    val endAddress: String,

    @JsonProperty("travel_time")
    val travelTime: String,

    @JsonProperty("ticket_type")
    val ticketType: TicketTypes,

    @JsonProperty("travel_route")
    val travelRoute: String,

    val distance: Double,
    val fee: Int,
)
