package com.example.cnpm.model.httpresponse

import com.example.cnpm.model.bike.dto.BikeDTO

data class BikeUpdateResponse(
    val action: String,
    val status: String,
    val target: BikeDTO?
)
