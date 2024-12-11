package com.cnpm.bikerentalapp.station.model.httprequest

import com.cnpm.bikerentalapp.config.utility.Geolocation
import com.cnpm.bikerentalapp.station.model.types.StationStatus
import com.fasterxml.jackson.annotation.JsonProperty
import jakarta.validation.constraints.NotBlank
import java.util.*

data class StationUpdateRequest(
    @JsonProperty("station_id")
    val stationID: UUID?,

    @JsonProperty("region_id")
    val regionID: String?,
    val city: String?,

    @NotBlank(message = "Region number is required")
    @JsonProperty("region_num")
    val regionNum: Int?,

    val coordinates: Geolocation?,
    var name: String?,
    val address: String?,
    val capacity: Int?,
    val status: StationStatus?
)
