package com.example.cnpm.services

import com.example.cnpm.exception.BikeNotFoundException
import com.example.cnpm.exception.InvalidBikeChange
import com.example.cnpm.model.Bike
import com.example.cnpm.model.BikeStatus
import com.example.cnpm.model.BikeType
import com.example.cnpm.model.dto.BikeCreateRequest
import com.example.cnpm.model.dto.BikeDTO
import com.example.cnpm.model.dto.BikeUpdateRequest
import com.example.cnpm.repositories.BikeRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.util.ReflectionUtils
import java.lang.reflect.Field
import java.util.UUID
import kotlin.reflect.full.memberProperties

@Service
class BikeServices {

    @Autowired
    private lateinit var bikeRepo: BikeRepository

    private fun mapBikeCreateToEntity(bike: Bike, req: BikeCreateRequest) {
        bike.plate = req.plate
        bike.type = req.type
        bike.battery = req.battery ?: bike.battery
        bike.status = req.status ?: bike.status
    }

    private fun mapBikeToDTO(bike: Bike) = BikeDTO(
        id = bike.id,
        plate = bike.plate,
        type = bike.type,
        battery = bike.battery,
        status = bike.status
    )

    fun checkBikeExistsByID(id: UUID) {
        if (!bikeRepo.existsById(id))
            throw BikeNotFoundException("Bike with id $id not found")
    }

    fun checkBikeExistsByPlate(plate: String) {
        if (bikeRepo.checkBikeExistByPlate(plate) == 0)
            throw BikeNotFoundException("Bike with plate $plate not found")
    }


    fun getAllBikes() : List<BikeDTO> = bikeRepo.findAll().stream().map { mapBikeToDTO(it) }.toList()

    fun getBikeById(id: UUID) : BikeDTO  {
        checkBikeExistsByID(id)
        return bikeRepo.findById(id).get().let { mapBikeToDTO(it) }
    }

    fun getBikeByPlate(plate: String) : BikeDTO {
        checkBikeExistsByPlate(plate)
        return mapBikeToDTO(bikeRepo.getBikeByPlate(plate))
    }

    fun getBikeIdByPlate(plate: String) : UUID = bikeRepo.getBikeIdByPlate(plate)

    fun getBikeByType(type: BikeType) : List<BikeDTO> = bikeRepo.getBikeByType(type).stream().map { mapBikeToDTO(it) }.toList()

    fun getAvailableBikesByType(type: BikeType) : List<BikeDTO> = bikeRepo.getAvailableBikesByType(type).stream().map { mapBikeToDTO(it) }.toList()

    fun countBikesByType(type: BikeType) : Int = bikeRepo.countBikesByType(type)

    fun addBike(req: BikeCreateRequest): BikeDTO {
        val newBike = Bike()
        mapBikeCreateToEntity(newBike, req)
        bikeRepo.save(newBike)
        return mapBikeToDTO(newBike)
    }

    fun updateBike(id: UUID, req: BikeUpdateRequest): BikeDTO {
        val targetBike: Bike = bikeRepo.getReferenceById(id)
        for (prop in BikeUpdateRequest::class.memberProperties) {
            if (prop.get(req) != null) {
                val field: Field? = ReflectionUtils.findField(Bike::class.java, prop.name)
                field?.let {
                    it.isAccessible = true
                    if (prop.name == "status" && prop.get(req) == BikeStatus.CHARGING)
                        if (targetBike.type != BikeType.ELECTRIC)
                            throw InvalidBikeChange("Only electric bikes can be set to charging status")

                    ReflectionUtils.setField(it, targetBike, prop.get(req))
                }
            }
        }
        val savedBike = bikeRepo.save(targetBike)
        return mapBikeToDTO(savedBike)
    }

    fun deleteBike(plate: String) {
        val id: UUID = bikeRepo.getBikeIdByPlate(plate)
        val bike: Bike = bikeRepo.getReferenceById(id)
        bikeRepo.delete(bike)
    }

    fun deleteAllBikes() = bikeRepo.deleteAll()
}