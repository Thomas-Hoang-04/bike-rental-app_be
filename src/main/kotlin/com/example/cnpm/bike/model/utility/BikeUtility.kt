package com.example.cnpm.bike.model.utility

import com.example.cnpm.exception.DataNotFoundException
import com.example.cnpm.exception.InvalidUpdate
import com.example.cnpm.bike.model.dto.bike.BikeCreateRequest
import com.example.cnpm.bike.model.dto.bike.BikeDTO
import com.example.cnpm.bike.model.entity.Bike
import com.example.cnpm.bike.model.types.BikeStatus
import com.example.cnpm.bike.model.types.BikeType
import com.example.cnpm.bike.repositories.BikeRepository
import org.springframework.stereotype.Component
import java.util.UUID

@Component
class BikeUtility(private val bikeRepo: BikeRepository) {
    fun mapBikeCreateToEntity(bike: Bike, req: BikeCreateRequest) {
        bike.plate = req.plate
        bike.type = req.type
        bike.battery = req.battery ?: bike.battery
        bike.status = req.status ?: bike.status
        bike.location = req.location ?: bike.location
    }

    fun mapBikeToDTO(bike: Bike) = BikeDTO(
        id = bike.id,
        plate = bike.plate,
        type = bike.type,
        battery = bike.battery,
        status = bike.status,
        location = bike.location
    )

    fun checkBikeExistsByID(id: UUID) {
        if (!bikeRepo.existsById(id))
            throw DataNotFoundException("Bike with id $id not found")
    }

    fun verifyBikePlate(plate: String, type: BikeType) {
        if (plate[0] == 'E' && type == BikeType.ELECTRIC) return
        if (plate[0] == 'X' && type == BikeType.MANUAL) return
        throw InvalidUpdate("Plate $plate does not match bike type ${type.toString().lowercase()}")
    }

    fun verifyBikeStatus(status: BikeStatus, type: BikeType) {
        if (status == BikeStatus.CHARGING && type == BikeType.ELECTRIC) return
        if (status == BikeStatus.AVAILABLE || status == BikeStatus.IN_USE) return
        throw InvalidUpdate("Only electric bikes can set status to CHARGING")
    }

    fun checkAvailableSpaceByLocation(location: String, capacity: Int) {
        if (bikeRepo.checkBikeCountByLocation(location) >= capacity)
            throw InvalidUpdate("Bike count at location $location exceeds maximum capacity of $capacity")
    }
}