package com.cnpm.bikerentalapp.station.model.httprequest

import com.cnpm.bikerentalapp.config.utility.Geolocation
import com.cnpm.bikerentalapp.station.model.types.StationStatus
import com.fasterxml.jackson.annotation.JsonProperty
import jakarta.validation.constraints.NotBlank

data class StationCreateRequest(
    @JsonProperty("region_id")
    val regionID: String?,
    val city: String?,

    @NotBlank(message = "Station coordinates are required")
    val coordinates: Geolocation,

    @NotBlank(message = "Station name is required")
    var name: String,

    @NotBlank(message = "Station address is required")
    val address: String,

    @NotBlank(message = "Station capacity is required")
    val capacity: Int?,

    val status: StationStatus?
)
