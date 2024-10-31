package com.example.cnpm.bike.model.dto.station

import com.example.cnpm.bike.model.types.BikeStationStatus

data class BikeStationDTO(
    val locationID: String,
    val regionID: String,
    val regionNum: Int,
    var name: String,
    val address: String,
    val capacity: Int,
    val status: BikeStationStatus
)
