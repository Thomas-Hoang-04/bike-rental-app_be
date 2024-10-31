package com.example.cnpm.bike.model.httpresponse

import com.example.cnpm.bike.model.dto.bike.BikeDTO

data class BikeQueryResponse(
    val queryBy: String,
    val params: Map<String, String>,
    val bikes: List<BikeDTO>
)