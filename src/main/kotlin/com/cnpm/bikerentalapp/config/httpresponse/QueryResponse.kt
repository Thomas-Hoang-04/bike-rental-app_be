package com.cnpm.bikerentalapp.config.httpresponse

import com.fasterxml.jackson.annotation.JsonProperty

data class QueryResponse<P, T>(
    @JsonProperty("query_by")
    val queryBy: String,
    @JsonProperty("target_count")
    val targetCount: Int,
    val params: Map<String, P>,
    val data: List<T>
)
