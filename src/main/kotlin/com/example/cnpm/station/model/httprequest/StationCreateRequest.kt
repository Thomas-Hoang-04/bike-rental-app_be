package com.example.cnpm.station.model.httprequest

import com.example.cnpm.station.model.types.StationStatus
import com.fasterxml.jackson.annotation.JsonProperty
import jakarta.validation.constraints.NotBlank

data class StationCreateRequest(
    @JsonProperty("region_id")
    val regionID: String?,
    val city: String?,

    @NotBlank(message = "Station coordinates are required")
    val longitude: Double,

    @NotBlank(message = "Station coordinates are required")
    val latitude: Double,

    @NotBlank(message = "Station name is required")
    var name: String,

    @NotBlank(message = "Station address is required")
    val address: String,

    @NotBlank(message = "Station capacity is required")
    val capacity: Int?,

    val status: StationStatus?
)
