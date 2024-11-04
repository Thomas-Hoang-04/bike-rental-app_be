package com.cnpm.bikerentalapp.station.model.httpresponse

import com.cnpm.bikerentalapp.station.model.dto.StationDTO

data class StationUpdateResponse(
    val action: String,
    val status: String,
    val target: StationDTO?
)
