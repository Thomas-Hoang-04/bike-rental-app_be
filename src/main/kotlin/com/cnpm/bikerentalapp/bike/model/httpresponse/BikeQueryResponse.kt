package com.cnpm.bikerentalapp.bike.model.httpresponse

import com.cnpm.bikerentalapp.bike.model.dto.BikeDTO
import com.fasterxml.jackson.annotation.JsonProperty

data class BikeQueryResponse<T>(
    @JsonProperty("query_by")
    val queryBy: String,
    @JsonProperty("target_count")
    val targetCount: Int,
    val params: Map<String, T>,
    val bikes: List<BikeDTO>
)