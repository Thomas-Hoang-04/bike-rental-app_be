package com.cnpm.bikerentalapp.station.model.httprequest

import com.cnpm.bikerentalapp.station.model.types.StationStatus
import com.fasterxml.jackson.annotation.JsonProperty
import jakarta.validation.constraints.NotBlank

data class StationUpdateRequest(
    @JsonProperty("region_id")
    val regionID: String?,
    val city: String?,

    @NotBlank(message = "Region number is required")
    @JsonProperty("region_num")
    val regionNum: Int,

    val latitude: Double?,
    val longitude: Double?,
    var name: String?,
    val address: String?,
    val capacity: Int?,
    val status: StationStatus?
)
