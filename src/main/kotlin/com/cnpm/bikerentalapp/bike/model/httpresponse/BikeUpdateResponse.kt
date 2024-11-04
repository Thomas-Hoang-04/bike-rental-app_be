package com.cnpm.bikerentalapp.bike.model.httpresponse

import com.cnpm.bikerentalapp.bike.model.dto.BikeDTO

data class BikeUpdateResponse(
    val action: String,
    val status: String,
    val target: BikeDTO?
)
