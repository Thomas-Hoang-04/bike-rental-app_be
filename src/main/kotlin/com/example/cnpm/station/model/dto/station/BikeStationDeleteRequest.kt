package com.example.cnpm.station.model.dto.station

import jakarta.validation.constraints.NotBlank

data class BikeStationDeleteRequest(
    val stationID: String?,

    @NotBlank(message = "Region ID is required")
    val regionID: String,

    @NotBlank(message = "Region number is required")
    val regionNum: Int
)
