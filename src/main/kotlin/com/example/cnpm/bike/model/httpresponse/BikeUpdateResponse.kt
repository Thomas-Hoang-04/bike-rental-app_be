package com.example.cnpm.bike.model.httpresponse

import com.example.cnpm.bike.model.dto.bike.BikeDTO

data class BikeUpdateResponse(
    val action: String,
    val status: String,
    val target: BikeDTO?
)
