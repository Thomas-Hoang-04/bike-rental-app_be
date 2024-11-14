package com.cnpm.bikerentalapp.config.httpresponse

import com.fasterxml.jackson.annotation.JsonProperty

data class CRUDResponse<T>(
    val action: String,
    val status: String,
    @JsonProperty("target_count")
    val targetCount: Int = 1,
    val target: T?
)
