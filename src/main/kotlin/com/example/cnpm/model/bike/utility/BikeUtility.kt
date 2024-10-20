package com.example.cnpm.model.bike.utility

import com.example.cnpm.exception.bike.BikeNotFoundException
import com.example.cnpm.exception.bike.InvalidBikeUpdate
import com.example.cnpm.model.bike.dto.BikeCreateRequest
import com.example.cnpm.model.bike.dto.BikeDTO
import com.example.cnpm.model.bike.entity.Bike
import com.example.cnpm.model.bike.types.BikeStatus
import com.example.cnpm.model.bike.types.BikeType
import com.example.cnpm.repositories.bike.BikeRepository
import org.springframework.stereotype.Component
import java.util.UUID

@Component
class BikeUtility(private val bikeRepo: BikeRepository) {
    fun mapBikeCreateToEntity(bike: Bike, req: BikeCreateRequest) {
        bike.plate = req.plate
        bike.type = req.type
        bike.battery = req.battery ?: bike.battery
        bike.status = req.status ?: bike.status
    }

    fun mapBikeToDTO(bike: Bike) = BikeDTO(
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

    fun verifyBikePlate(plate: String, type: BikeType) {
        if (plate[0] == 'E' && type == BikeType.ELECTRIC) return
        if (plate[0] == 'X' && type == BikeType.MANUAL) return
        throw InvalidBikeUpdate("Plate $plate does not match bike type ${type.toString().lowercase()}")
    }

    fun verifyBikeStatus(status: BikeStatus, type: BikeType) {
        if (status == BikeStatus.CHARGING && type == BikeType.ELECTRIC) return
        throw InvalidBikeUpdate("Only electric bikes can set status to CHARGING")
    }
}