package com.cnpm.bikerentalapp.station.services

import com.cnpm.bikerentalapp.config.exception.model.DataNotFoundException
import com.cnpm.bikerentalapp.config.exception.model.InvalidUpdate
import com.cnpm.bikerentalapp.station.model.dto.StationDTO
import com.cnpm.bikerentalapp.station.model.entity.BikeStation
import com.cnpm.bikerentalapp.station.model.httprequest.StationCreateRequest
import com.cnpm.bikerentalapp.station.model.httprequest.StationDeleteRequest
import com.cnpm.bikerentalapp.station.model.httprequest.StationUpdateRequest
import com.cnpm.bikerentalapp.station.model.types.StationStatus
import com.cnpm.bikerentalapp.station.model.utility.StationUtility
import com.cnpm.bikerentalapp.station.repository.StationRepository
import org.springframework.stereotype.Service
import org.springframework.util.ReflectionUtils
import java.lang.reflect.Field
import java.text.DecimalFormat
import java.util.UUID
import kotlin.reflect.full.memberProperties

@Service
class StationServices(
    private val util: StationUtility,
    private val stationRepo: StationRepository
) {

    fun getAllStations() : List<StationDTO> = stationRepo.findAll().map {
        it.mapStationToDTO() }.toList()

    fun getStationByID(id: UUID): BikeStation {
        util.checkStationExistsByID(id)
        return stationRepo.findById(id).get()
    }

    fun getStationByRegionID(regionID: String, regionNum: Int) : StationDTO {
        val station = stationRepo.getStationByRegionID(regionID, regionNum).orElseThrow {
            throw DataNotFoundException("Station with regionID $regionID${DecimalFormat("000").format(regionNum)} not found")
        }
        return station.mapStationToDTO()
    }

    fun getStationsByRegion(regionID: String) : List<StationDTO> = stationRepo.getStationsByRegion(regionID).map {
        it.mapStationToDTO() }.toList()

    fun getStationsByCity(city: String) : List<StationDTO> = stationRepo.getStationsByCity(city).map {
        it.mapStationToDTO() }.toList()

    fun getAvailableStations() : List<StationDTO> = stationRepo.getAvailableStations().map {
        it.mapStationToDTO() }.toList()

    fun getNearbyStations(lat: Double, long: Double, radius: Double) : List<StationDTO> {
        val stations = stationRepo.getNearbyStations(lat, long, radius)
        return stations.map { it.mapStationToDTO() }.toList()
    }

    fun addStation(req: StationCreateRequest) : StationDTO {
        val regionCodex: Pair<String, Int> = util.extractRegionCodex(req.city, req.regionID)
        if (!util.verifyStationLocation(req.latitude, req.longitude))
            throw InvalidUpdate("Invalid location coordinates")
        val newStation = BikeStation(
            regionID = regionCodex.first,
            regionNum = regionCodex.second,
            latitude = req.latitude,
            longitude = req.longitude,
            name = req.name,
            address = req.address,
            capacity = req.capacity ?: 0,
            status = req.status ?: StationStatus.ACTIVE
        )
        stationRepo.save(newStation)
        return newStation.mapStationToDTO()
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
        targetStation.createGeoLocation()
        return stationRepo.save(targetStation).mapStationToDTO()

    }

    fun deleteStation(req: StationDeleteRequest) {
        if (req.stationID != null) {
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