package com.example.cnpm.station.model.dto.station

import com.example.cnpm.station.model.types.BikeStationStatus
import jakarta.validation.constraints.NotBlank

data class BikeStationCreateRequest(
    @NotBlank(message = "Station location is required")
    val locationID: String,
    val regionID: String?,
    val city: String?,

    @NotBlank(message = "Station name is required")
    var name: String,

    @NotBlank(message = "Station address is required")
    val address: String,

    @NotBlank(message = "Station capacity is required")
    val capacity: Int?,

    val status: BikeStationStatus?
)
