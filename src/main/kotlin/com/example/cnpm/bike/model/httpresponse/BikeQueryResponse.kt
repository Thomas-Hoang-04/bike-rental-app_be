package com.example.cnpm.bike.model.httpresponse

import com.example.cnpm.bike.model.dto.BikeDTO
import com.fasterxml.jackson.annotation.JsonProperty

data class BikeQueryResponse<T>(
    @JsonProperty("query_by")
    val queryBy: String,
    val params: Map<String, T>,
    val bikes: List<BikeDTO>
)