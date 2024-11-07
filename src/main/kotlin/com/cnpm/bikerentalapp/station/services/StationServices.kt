package com.cnpm.bikerentalapp.station.services

import com.cnpm.bikerentalapp.station.model.dto.StationDTO
import com.cnpm.bikerentalapp.station.model.entity.BikeStation
import com.cnpm.bikerentalapp.station.model.httprequest.StationCreateRequest
import com.cnpm.bikerentalapp.station.model.httprequest.StationDeleteRequest
import com.cnpm.bikerentalapp.station.model.httprequest.StationUpdateRequest
import com.cnpm.bikerentalapp.station.model.utility.StationUtility
import com.cnpm.bikerentalapp.station.repository.StationRepository
import com.cnpm.bikerentalapp.exception.model.DataNotFoundException
import com.cnpm.bikerentalapp.exception.model.InvalidUpdate
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.util.ReflectionUtils
import java.lang.reflect.Field
import java.text.DecimalFormat
import java.util.*
import kotlin.reflect.full.memberProperties

@Service
class StationServices(private val util: StationUtility) {

    @Autowired
    private lateinit var stationRepo: StationRepository

    fun getAllStations() : List<StationDTO> = stationRepo.findAll().stream().map {
        util.mapStationToDTO(it) }.toList()

    fun getStationByID(id: UUID) : StationDTO {
        util.checkStationExistsByID(id)
        return stationRepo.findById(id).get().let { util.mapStationToDTO(it) }
    }

    fun getStationByRegionID(regionID: String, regionNum: Int) : StationDTO {
        val station = stationRepo.getStationByRegionID(regionID, regionNum).orElseThrow {
            throw DataNotFoundException("Station with regionID $regionID${DecimalFormat("000").format(regionNum)} not found")
        }
        return util.mapStationToDTO(station)
    }

    fun getStationsByRegion(regionID: String) : List<StationDTO> = stationRepo.getStationsByRegion(regionID).stream().map {
        util.mapStationToDTO(it) }.toList()

    fun getStationsByCity(city: String) : List<StationDTO> = stationRepo.getStationsByCity(city).stream().map {
        util.mapStationToDTO(it) }.toList()

    fun getAvailableStations() : List<StationDTO> = stationRepo.getAvailableStations().stream().map {
        util.mapStationToDTO(it) }.toList()

    fun getNearbyStations(lat: Double, long: Double, radius: Double) : List<StationDTO> {
        val stations = stationRepo.getNearbyStations(lat, long, radius)
        return stations.stream().map { util.mapStationToDTO(it) }.toList()
    }

    fun addStation(req: StationCreateRequest) : StationDTO {
        val newStation = BikeStation()
        if (!util.verifyStationLocation(req.latitude, req.longitude))
            throw InvalidUpdate("Invalid location coordinates")
        util.mapStationCreateToEntity(newStation, req)
        stationRepo.save(newStation)
        return util.mapStationToDTO(newStation)
    }

    fun updateStation(req: StationUpdateRequest): StationDTO {
        val regionID: String = req.regionID ?: stationRepo.getRegionIDByCity(req.city ?: "")
                .orElseThrow { DataNotFoundException("City $req.city not found") }
        val targetStation: BikeStation = stationRepo.getStationByRegionID(regionID, req.regionNum).orElseThrow {
            throw DataNotFoundException("Station with regionID ${req.regionID}${DecimalFormat("000").format(req.regionNum)} not found")
        }
        for (prop in StationUpdateRequest::class.memberProperties) {
            if (prop.name == "regionID" || prop.name == "regionNum" || prop.name == "city") continue
            if (prop.get(req) != null) {
                val field: Field = BikeStation::class.java.getDeclaredField(prop.name)
                field.isAccessible = true
                ReflectionUtils.setField(field, targetStation, prop.get(req))
            }
        }
        targetStation.geoLocation = util.createGeoLocation(targetStation.latitude, targetStation.longitude)
        val savedStation: BikeStation = stationRepo.save(targetStation)
        return util.mapStationToDTO(savedStation)
    }

    fun deleteStation(req: StationDeleteRequest) {
        if (req.stationID != null) {
            util.checkStationExistsByID(req.stationID)
            stationRepo.deleteById(req.stationID)
        } else {
            val regionID: String = req.regionID ?: stationRepo.getRegionIDByCity(req.city ?: "")
                .orElseThrow { DataNotFoundException("City $req.city not found") }
            val station = stationRepo.getStationByRegionID(regionID, req.regionNum).orElseThrow {
                throw DataNotFoundException("Station with regionID ${regionID}${DecimalFormat("000").format(req.regionNum)} not found")
            }
            stationRepo.delete(station)
        }
    }
}