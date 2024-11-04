package com.cnpm.bikerentalapp.station.model.utility

import com.cnpm.bikerentalapp.station.model.dto.StationDTO
import com.cnpm.bikerentalapp.station.model.entity.BikeStation
import com.cnpm.bikerentalapp.station.model.httprequest.StationCreateRequest
import com.cnpm.bikerentalapp.station.repositories.StationRepository
import com.cnpm.bikerentalapp.utility.exception.DataNotFoundException
import org.locationtech.jts.geom.Coordinate
import org.locationtech.jts.geom.GeometryFactory
import org.locationtech.jts.geom.Point
import org.springframework.stereotype.Component
import java.util.*

@Component
class StationUtility(private val stationRepo: StationRepository) {
    fun mapStationToDTO(station: BikeStation) = StationDTO(
        stationID = station.stationID,
        regionID = station.regionID,
        regionNum = station.regionNum,
        name = station.name,
        address = station.address,
        capacity = station.capacity,
        status = station.status,
        latitude = station.latitude,
        longitude = station.longitude,
    )

    fun mapStationCreateToEntity(station: BikeStation, req: StationCreateRequest) {
        if (req.regionID == null) {
            station.regionID = stationRepo.getRegionIDByCity(req.city ?: "")
                .orElseThrow { DataNotFoundException("City $req.city not found") }
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
        station.latitude = req.latitude
        station.longitude = req.longitude
        station.geoLocation = createGeoLocation(req.latitude, req.longitude)
    }

    fun checkStationExistsByID(id: UUID) {
        if (!stationRepo.existsById(id))
            throw DataNotFoundException("Station with id $id not found")
    }

    fun verifyStationLocation(lat: Double, long: Double): Boolean
        = lat in -90.0..90.0 && long in -180.0..180.0

    fun createGeoLocation(lat: Double, long: Double): Point {
        return GeometryFactory().createPoint(Coordinate(long, lat))
    }
}