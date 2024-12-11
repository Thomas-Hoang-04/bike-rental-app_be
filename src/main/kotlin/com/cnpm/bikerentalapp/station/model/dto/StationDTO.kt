package com.cnpm.bikerentalapp.station.model.dto

import com.cnpm.bikerentalapp.bike.model.dto.BikeDTO
import com.cnpm.bikerentalapp.config.utility.Geolocation
import com.cnpm.bikerentalapp.station.model.types.StationStatus
import com.fasterxml.jackson.annotation.JsonProperty
import java.util.*

data class StationDTO(
    @JsonProperty("station_id")
    val stationID: UUID,

    @JsonProperty("region_id")
    val regionID: String,

    @JsonProperty("region_num")
    val regionNum: Int,
    val coordinates: Geolocation,
    var name: String,
    val address: String,
    val capacity: Int,
    val status: StationStatus,

    @JsonProperty("bike_list")
    val bikeList: List<BikeDTO>
)
