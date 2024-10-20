package com.example.cnpm.model.httpresponse

import com.example.cnpm.model.bike.dto.BikeDTO

data class BikeQueryResponse(
    val queryBy: String,
    val params: Map<String, String>,
    val bikes: List<BikeDTO>
)