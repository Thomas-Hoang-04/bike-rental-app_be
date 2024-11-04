package com.example.cnpm.station.model.httpresponse

import com.example.cnpm.station.model.dto.StationDTO

data class StationUpdateResponse(
    val action: String,
    val status: String,
    val target: StationDTO?
)
