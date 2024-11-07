package com.cnpm.bikerentalapp.bike.services

import com.cnpm.bikerentalapp.bike.model.dto.BikeDTO
import com.cnpm.bikerentalapp.bike.model.entity.Bike
import com.cnpm.bikerentalapp.bike.model.httprequest.BikeCreateRequest
import com.cnpm.bikerentalapp.bike.model.httprequest.BikeUpdateRequest
import com.cnpm.bikerentalapp.bike.model.types.BikeType
import com.cnpm.bikerentalapp.bike.model.utility.BikeUtility
import com.cnpm.bikerentalapp.bike.repository.BikeRepository
import com.cnpm.bikerentalapp.exception.model.DataNotFoundException
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.util.ReflectionUtils
import java.lang.reflect.Field
import java.util.UUID
import java.util.Optional
import kotlin.reflect.full.memberProperties

@Service
class BikeServices(private val util: BikeUtility) {

    @Autowired
    private lateinit var bikeRepo: BikeRepository

    fun getAllBikes() : List<BikeDTO> = bikeRepo.findAll().map { util.mapBikeToDTO(it) }.toList()

    fun getBikeById(id: UUID) : BikeDTO {
        util.checkBikeExistsByID(id)
        return bikeRepo.findById(id).get().let { util.mapBikeToDTO(it) }
    }

    fun getBikeByPlate(plate: String) : BikeDTO {
        val bike: Optional<Bike> = bikeRepo.getBikeByPlate(plate)
        if (bike.isEmpty) throw DataNotFoundException("Bike with plate $plate not found")
        return util.mapBikeToDTO(bike.get())
    }

    fun getBikeIdByPlate(plate: String) : UUID {
        val id: Optional<UUID> = bikeRepo.getBikeIdByPlate(plate)
        if (id.isEmpty) throw DataNotFoundException("Bike with plate $plate not found")
        return id.get()
    }

    fun getBikeByType(type: BikeType) : List<BikeDTO> = bikeRepo.getBikeByType(type.name).map { util.mapBikeToDTO(it) }.toList()

    fun getAvailableBikes() : List<BikeDTO> = bikeRepo.getAvailableBikes().map { util.mapBikeToDTO(it) }.toList()

    fun getAvailableBikesByType(type: BikeType) : List<BikeDTO> = bikeRepo.getAvailableBikesByType(type.name).map { util.mapBikeToDTO(it) }.toList()

    fun countBikesByType(type: BikeType) : Int = bikeRepo.countBikesByType(type.name)

    fun addBike(req: BikeCreateRequest): BikeDTO {
        util.verifyBikePlate(req.plate, req.type)
        val newBike = Bike()
        util.mapBikeCreateToEntity(newBike, req)
        bikeRepo.save(newBike)
        return util.mapBikeToDTO(newBike)
    }

    fun updateBike(id: UUID, req: BikeUpdateRequest, capacity: Int?): BikeDTO {
        val targetBike: Bike = bikeRepo.findById(id).orElseThrow { DataNotFoundException("Bike with id $id not found") }
        if (req.newPlate != null) util.verifyBikePlate(req.newPlate, req.type ?: targetBike.type)
        if (req.status != null) util.verifyBikeStatus(req.status, req.type ?: targetBike.type)
        if (capacity != null) util.checkAvailableSpaceByLocation(req.location!!, capacity)
        for (prop in BikeUpdateRequest::class.memberProperties) {
            if (prop.name == "plate") continue
            if (prop.get(req) != null) {
                val field: Field = Bike::class.java.getDeclaredField(if (prop.name == "newPlate") "plate" else prop.name)
                field.isAccessible = true
                ReflectionUtils.setField(field, targetBike, prop.get(req))
            }
        }
        val savedBike: Bike = bikeRepo.save(targetBike)
        return util.mapBikeToDTO(savedBike)
    }

    fun deleteBike(plate: String) {
        val id: UUID = bikeRepo.getBikeIdByPlate(plate).get()
        val bike: Bike = bikeRepo.getReferenceById(id)
        bikeRepo.delete(bike)
    }

    // fun deleteAllBikes() = bikeRepo.deleteAll()
}