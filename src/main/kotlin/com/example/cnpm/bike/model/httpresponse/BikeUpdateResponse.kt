package com.example.cnpm.bike.model.httpresponse

import com.example.cnpm.bike.model.dto.BikeDTO

data class BikeUpdateResponse(
    val action: String,
    val status: String,
    val target: BikeDTO?
)
