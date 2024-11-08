package com.cnpm.bikerentalapp.station.model.httpresponse

import com.cnpm.bikerentalapp.station.model.dto.StationDTO
import com.fasterxml.jackson.annotation.JsonProperty

data class StationQueryResponse<T>(
    @JsonProperty("query_by")
    val queryBy: String,
    @JsonProperty("target_count")
    val targetCount: Int,
    val params: Map<String, T>,
    val stations: List<StationDTO>
)
