package com.example.cnpm.station.model.utility

import com.example.cnpm.exception.DataNotFoundException
import com.example.cnpm.station.model.dto.station.BikeStationCreateRequest
import com.example.cnpm.station.model.dto.station.BikeStationDTO
import com.example.cnpm.station.model.entity.BikeStation
import com.example.cnpm.station.repositories.BikeStationRepository
import com.google.openlocationcode.OpenLocationCode
import org.springframework.stereotype.Component

@Component
class BikeStationUtility(private val stationRepo: BikeStationRepository) {
    fun mapStationToDTO(station: BikeStation) = BikeStationDTO(
        locationID = station.locationID,
        regionID = station.regionID,
        regionNum = station.regionNum,
        name = station.name,
        address = station.address,
        capacity = station.capacity,
        status = station.status,
    )

    fun mapStationCreateToEntity(station: BikeStation, req: BikeStationCreateRequest) {
        if (req.regionID == null) {
            station.regionID = stationRepo.getRegionIDByCity(req.city ?: req.locationID.split(", ")[1]).orElseThrow { DataNotFoundException("City $req.city not found") }
            station.regionNum = stationRepo.getRegionalStationMaxNum(station.regionID).get() + 1
        } else {
            station.regionID = req.regionID
            station.regionNum = stationRepo.getRegionalStationMaxNum(req.regionID).orElseThrow {
                DataNotFoundException("Region with id ${req.regionID} not found") } + 1
        }
        station.name = req.name
        station.address = req.address
        station.capacity = req.capacity ?: station.capacity
        station.status = req.status ?: station.status
    }

    fun checkStationExistsByID(id: String) {
        if (!stationRepo.existsById(id))
            throw DataNotFoundException("Station with id $id not found")
    }

    fun verifyStationIDFormat(id: String): Boolean {
        val extractedId: List<String> = id.split(", ")
        return OpenLocationCode.isValidCode(extractedId[0].split(" ")[0])
                && extractedId.size == 3
                && stationRepo.getRegionIDByCity(extractedId[1]).isPresent
    }
}