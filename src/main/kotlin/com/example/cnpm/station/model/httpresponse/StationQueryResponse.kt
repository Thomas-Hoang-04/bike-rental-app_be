package com.example.cnpm.station.model.httpresponse

import com.example.cnpm.station.model.dto.StationDTO
import com.fasterxml.jackson.annotation.JsonProperty

data class StationQueryResponse<T>(
    @JsonProperty("query_by")
    val queryBy: String,
    val params: Map<String, T>,
    val stations: List<StationDTO>
)
