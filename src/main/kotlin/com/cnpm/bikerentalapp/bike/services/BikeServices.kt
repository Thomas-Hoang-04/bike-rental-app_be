package com.cnpm.bikerentalapp.bike.services

import com.cnpm.bikerentalapp.bike.model.dto.BikeDTO
import com.cnpm.bikerentalapp.bike.model.entity.Bike
import com.cnpm.bikerentalapp.bike.model.httprequest.BikeCreateRequest
import com.cnpm.bikerentalapp.bike.model.httprequest.BikeRenting
import com.cnpm.bikerentalapp.bike.model.httprequest.BikeUpdateRequest
import com.cnpm.bikerentalapp.bike.model.types.BikeAction
import com.cnpm.bikerentalapp.bike.model.types.BikeStatus
import com.cnpm.bikerentalapp.bike.model.types.BikeType
import com.cnpm.bikerentalapp.bike.model.utility.BikeUtility
import com.cnpm.bikerentalapp.bike.repository.BikeRepository
import com.cnpm.bikerentalapp.config.exception.model.DataNotFoundException
import com.cnpm.bikerentalapp.config.exception.model.InvalidQuery
import com.cnpm.bikerentalapp.config.exception.model.InvalidUpdate
import com.cnpm.bikerentalapp.station.model.dto.StationDTO
import com.cnpm.bikerentalapp.station.model.entity.BikeStation
import com.cnpm.bikerentalapp.station.services.StationServices
import org.springframework.stereotype.Service
import org.springframework.util.ReflectionUtils
import java.lang.reflect.Field
import java.util.*
import kotlin.reflect.full.memberProperties

@Service
class BikeServices(
    private val util: BikeUtility,
    private val bikeRepo: BikeRepository,
    private val stationServices: StationServices
) {

    fun getAllBikes() : List<BikeDTO> = bikeRepo.findAll().map { it.mapBikeToDTO() }.toList()

    fun getBikeByID(id: UUID) : Bike
        = bikeRepo.findById(id).orElseThrow { DataNotFoundException("Bike with ID $id not found") }

    fun getBikeByPlate(plate: String) : Bike = bikeRepo.getBikeByPlate(plate).orElseThrow {
            DataNotFoundException("Bike with plate $plate not found")
        }

    fun getBikeByType(type: BikeType) : List<BikeDTO> = bikeRepo.getBikeByType(type.name).map { it.mapBikeToDTO() }.toList()

    fun getAvailableBikes() : List<BikeDTO> = bikeRepo.getAvailableBikes().map { it.mapBikeToDTO() }.toList()

    fun getAvailableBikesByType(type: BikeType) : List<BikeDTO> = bikeRepo.getAvailableBikesByType(type.name).map { it.mapBikeToDTO() }.toList()

    fun rentingBike(req: BikeRenting): BikeDTO {
        val targetBike: Bike = getBikeByPlate(req.plate)
        val nearbyStation: StationDTO = stationServices.getNearbyStations(req.latitude, req.longitude, 20.0).firstOrNull()
            ?: throw DataNotFoundException("No valid nearby station found")
        if (req.action == BikeAction.RENT) {
            if (targetBike.mapBikeToDTO().status == BikeStatus.IN_USE) throw InvalidQuery("Bike is not available")
            if (!nearbyStation.bikeList.contains(targetBike.mapBikeToDTO())) throw InvalidQuery("Bike is not in the station")
            for (prop in mapOf("location" to null, "status" to BikeStatus.IN_USE)) {
                val field: Field = Bike::class.java.getDeclaredField(prop.key)
                field.isAccessible = true
                ReflectionUtils.setField(field, targetBike, prop.value)
            }
        } else if (req.action == BikeAction.RETURN) {
            if (targetBike.mapBikeToDTO().status != BikeStatus.IN_USE) throw InvalidUpdate("Bike is not in use")
            util.checkAvailableSpaceByLocation(nearbyStation.stationID, nearbyStation.capacity)
            val battery: Int = req.battery ?: (targetBike.mapBikeToDTO().battery - 10)
            for (prop in mapOf(
                    "location" to stationServices.getStationByID(nearbyStation.stationID),
                    "status" to BikeStatus.AVAILABLE,
                    "battery" to battery
            )) {
                val field: Field = Bike::class.java.getDeclaredField(prop.key)
                field.isAccessible = true
                ReflectionUtils.setField(field, targetBike, prop.value)
            }
        }
        return bikeRepo.save(targetBike).mapBikeToDTO()
    }

    fun addBike(req: BikeCreateRequest): BikeDTO {
        util.verifyBikePlate(req.plate, req.type)
        val newBike = Bike(
            plate = req.plate,
            type = req.type,
            location = if (req.location != null) stationServices.getStationByID(req.location) else null ,
            battery = req.battery ?: 100,
            status = req.status ?: BikeStatus.AVAILABLE
        )
        bikeRepo.save(newBike)
        return newBike.mapBikeToDTO()
    }

    fun updateBike(req: BikeUpdateRequest): BikeDTO {
        val targetBike: Bike = getBikeByPlate(req.plate)
        if (targetBike.mapBikeToDTO().status == BikeStatus.IN_USE) throw InvalidUpdate("Bike is currently in use")
        if (req.newPlate != null) util.verifyBikePlate(req.newPlate, req.type ?: targetBike.mapBikeToDTO().type)
        if (req.status != null) util.verifyBikeChargingStatus(req.status, req.type ?: targetBike.mapBikeToDTO().type)
        val station: BikeStation? = if (req.location != null) stationServices.getStationByID(req.location) else null
        if (station != null) util.checkAvailableSpaceByLocation(req.location!!, station.publicCapacity)
        for (prop in BikeUpdateRequest::class.memberProperties) {
            if (prop.name == "plate") continue
            if (prop.get(req) != null) {
                val field: Field = Bike::class.java.getDeclaredField(if (prop.name == "newPlate") "plate" else prop.name)
                field.isAccessible = true
                ReflectionUtils.setField(field, targetBike, prop.get(req))
            }
        }
        return bikeRepo.save(targetBike).mapBikeToDTO()
    }

    fun deleteBike(plate: String) {
        val id: UUID = bikeRepo.getBikeIdByPlate(plate).get()
        val bike: Bike = bikeRepo.getReferenceById(id)
        bikeRepo.delete(bike)
    }
}