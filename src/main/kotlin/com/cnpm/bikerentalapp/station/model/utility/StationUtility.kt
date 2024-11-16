package com.cnpm.bikerentalapp.station.model.utility

import com.cnpm.bikerentalapp.config.exception.model.DataNotFoundException
import com.cnpm.bikerentalapp.station.repository.StationRepository
import org.springframework.stereotype.Component

@Component
class StationUtility(
    private val stationRepo: StationRepository
) {
    fun extractRegionCodex(city: String?, regionID: String?): Pair<String, Int> {
        if (regionID == null) {
            val newRegionID: String = stationRepo.getRegionIDByCity(city ?: "")
                .orElseThrow { DataNotFoundException("City $city not found") }
            val regionNum: Int = stationRepo.getRegionalStationMaxNum(newRegionID).get() + 1
            return Pair(newRegionID, regionNum)
        } else {
            val regionNum: Int = stationRepo.getRegionalStationMaxNum(regionID).orElseThrow {
               DataNotFoundException("Region with id $regionID not found") } + 1
            return Pair(regionID, regionNum)
        }
    }

    fun verifyStationLocation(lat: Double, long: Double): Boolean
        = lat in -90.0..90.0 && long in -180.0..180.0
}