package com.example.cnpm.services.bike

import com.example.cnpm.exception.bike.BikeNotFoundException
import com.example.cnpm.model.bike.entity.Bike
import com.example.cnpm.model.bike.dto.BikeCreateRequest
import com.example.cnpm.model.bike.dto.BikeDTO
import com.example.cnpm.model.bike.dto.BikeUpdateRequest
import com.example.cnpm.model.bike.utility.BikeUtility
import com.example.cnpm.repositories.bike.BikeRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.util.ReflectionUtils
import java.lang.reflect.Field
import java.util.UUID
import kotlin.reflect.full.memberProperties

@Service
class BikeServices(private val util: BikeUtility) {

    @Autowired
    private lateinit var bikeRepo: BikeRepository

    fun getAllBikes() : List<BikeDTO> = bikeRepo.findAll().stream().map { util.mapBikeToDTO(it) }.toList()


    fun getBikeById(id: UUID) : BikeDTO {
        util.checkBikeExistsByID(id)
        return bikeRepo.findById(id).get().let { util.mapBikeToDTO(it) }
    }

    fun getBikeByPlate(plate: String) : BikeDTO {
        util.checkBikeExistsByPlate(plate)
        return util.mapBikeToDTO(bikeRepo.getBikeByPlate(plate))
    }

    fun getBikeIdByPlate(plate: String) : UUID {
        util.checkBikeExistsByPlate(plate)
        return bikeRepo.getBikeIdByPlate(plate)
    }

    fun getBikeByType(type: String) : List<BikeDTO> = bikeRepo.getBikeByType(type).stream().map { util.mapBikeToDTO(it) }.toList()

    fun getAvailableBikes() : List<BikeDTO> = bikeRepo.getAvailableBikes().stream().map { util.mapBikeToDTO(it) }.toList()

    fun getAvailableBikesByType(type: String) : List<BikeDTO> = bikeRepo.getAvailableBikesByType(type).stream().map { util.mapBikeToDTO(it) }.toList()

    fun countBikesByType(type: String) : Int = bikeRepo.countBikesByType(type)

    fun addBike(req: BikeCreateRequest): BikeDTO {
        util.verifyBikePlate(req.plate, req.type)
        val newBike = Bike()
        util.mapBikeCreateToEntity(newBike, req)
        bikeRepo.save(newBike)
        return util.mapBikeToDTO(newBike)
    }

    fun updateBike(id: UUID, req: BikeUpdateRequest): BikeDTO {
        val targetBike: Bike = bikeRepo.findById(id).orElseThrow { BikeNotFoundException("Bike with id $id not found") }
        if (req.newPlate != null) util.verifyBikePlate(req.newPlate, req.type ?: targetBike.type)
        if (req.status != null) util.verifyBikeStatus(req.status, req.type ?: targetBike.type)
        for (prop in BikeUpdateRequest::class.memberProperties) {
            if (prop.name == "plate") continue
            if (prop.get(req) != null) {
                val field: Field = Bike::class.java.getDeclaredField(if (prop.name == "newPlate") "plate" else prop.name)
                field.isAccessible = true
                ReflectionUtils.setField(field, targetBike, prop.get(req))
            }
        }
        val savedBike = bikeRepo.save(targetBike)
        return util.mapBikeToDTO(savedBike)
    }

    fun deleteBike(plate: String) {
        util.checkBikeExistsByPlate(plate);
        val id: UUID = bikeRepo.getBikeIdByPlate(plate)
        val bike: Bike = bikeRepo.getReferenceById(id)
        bikeRepo.delete(bike)
    }

    // fun deleteAllBikes() = bikeRepo.deleteAll()
}