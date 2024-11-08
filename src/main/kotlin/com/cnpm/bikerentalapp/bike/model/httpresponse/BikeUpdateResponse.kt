package com.cnpm.bikerentalapp.bike.model.httpresponse

import com.cnpm.bikerentalapp.bike.model.dto.BikeDTO
import com.fasterxml.jackson.annotation.JsonProperty

data class BikeUpdateResponse(
    val action: String,
    val status: String,
    @JsonProperty("target_count")
    val targetCount: Int = 1,
    val target: BikeDTO?
)
