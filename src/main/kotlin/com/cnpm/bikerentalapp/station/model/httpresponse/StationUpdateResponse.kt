package com.cnpm.bikerentalapp.station.model.httpresponse

import com.cnpm.bikerentalapp.station.model.dto.StationDTO
import com.fasterxml.jackson.annotation.JsonProperty

data class StationUpdateResponse(
    val action: String,
    val status: String,
    @JsonProperty("target_count")
    val targetCount: Int = 1,
    val target: StationDTO?
)
