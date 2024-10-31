package com.example.cnpm.bike.services

import com.example.cnpm.bike.exception.DataNotFoundException
import com.example.cnpm.bike.exception.InvalidUpdate
import com.example.cnpm.bike.model.dto.station.BikeStationCreateRequest
import com.example.cnpm.bike.model.dto.station.BikeStationDTO
import com.example.cnpm.bike.model.dto.station.BikeStationDeleteRequest
import com.example.cnpm.bike.model.dto.station.BikeStationUpdateRequest
import com.example.cnpm.bike.model.entity.BikeStation
import com.example.cnpm.bike.model.utility.BikeStationUtility
import com.example.cnpm.bike.repositories.BikeStationRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class BikeStationServices(private val util: BikeStationUtility) {

    @Autowired
    private lateinit var stationRepo: BikeStationRepository

    fun getAllStations() : List<BikeStationDTO> = stationRepo.findAll().stream().map {
        util.mapStationToDTO(it) }.toList()

    fun getStationByID(id: String) : BikeStationDTO {
        util.checkStationExistsByID(id)
        return stationRepo.findById(id).get().let { util.mapStationToDTO(it) }
    }

    fun getStationByRegionID(regionID: String, regionNum: Int) : BikeStationDTO {
        val station = stationRepo.getStationByRegionID(regionID, regionNum).orElseThrow {
            throw DataNotFoundException("Station with regionID $regionID$regionNum not found")
        }
        return util.mapStationToDTO(station)
    }

    fun getStationCapacity(id: String) : Int {
        util.checkStationExistsByID(id)
        return stationRepo.getStationCapacity(id).orElseThrow {
            DataNotFoundException("Station with id $id not found") }
    }

    fun getStationsByRegion(regionID: String) : List<BikeStationDTO> = stationRepo.getStationsByRegion(regionID).stream().map {
        util.mapStationToDTO(it) }.toList()

    fun getStationsByCity(city: String) : List<BikeStationDTO> = stationRepo.getStationsByCity(city).stream().map {
        util.mapStationToDTO(it) }.toList()

    fun getAvailableStations() : List<BikeStationDTO> = stationRepo.getAvailableStations().stream().map {
        util.mapStationToDTO(it) }.toList()

    fun addStation(req: BikeStationCreateRequest) : BikeStationDTO {
        val newStation = BikeStation()
        if (!util.verifyStationIDFormat(req.locationID))
            throw InvalidUpdate("Invalid Plus Code location format")
        util.mapStationCreateToEntity(newStation, req)
        stationRepo.save(newStation)
        return util.mapStationToDTO(newStation)
    }

    fun updateStation(req: BikeStationUpdateRequest) {
        /* TODO: Implement updateStation */
    }

    fun deleteStation(req: BikeStationDeleteRequest) {
        if (req.stationID != null) {
            util.checkStationExistsByID(req.stationID)
            stationRepo.deleteById(req.stationID)
        } else {
            val station = stationRepo.getStationByRegionID(req.regionID, req.regionNum).orElseThrow {
                throw DataNotFoundException("Station with regionID ${req.regionID}${req.regionNum} not found")
            }
            stationRepo.delete(station)
        }
    }
}