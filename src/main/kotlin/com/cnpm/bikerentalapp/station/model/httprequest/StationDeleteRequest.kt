package com.cnpm.bikerentalapp.station.model.httprequest

import com.fasterxml.jackson.annotation.JsonProperty
import jakarta.validation.constraints.NotBlank
import java.util.*

data class StationDeleteRequest(
    @JsonProperty("station_id")
    val stationID: UUID?,

    @JsonProperty("region_id")
    val regionID: String?,
    val city: String?,

    @NotBlank(message = "Region number is required")
    @JsonProperty("region_num")
    val regionNum: Int
)
