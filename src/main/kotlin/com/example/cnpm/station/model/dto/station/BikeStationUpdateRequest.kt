package com.example.cnpm.station.model.dto.station

import com.example.cnpm.station.model.types.BikeStationStatus
import jakarta.validation.constraints.NotBlank

data class BikeStationUpdateRequest(
    val locationID: String?,

    @NotBlank(message = "Region ID is required")
    val regionID: String,
    val city: String?,

    @NotBlank(message = "Region number is required")
    val regionNum: Int,

    var name: String?,
    val address: String?,
    val capacity: Int?,
    val status: BikeStationStatus?
)
