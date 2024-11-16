package com.cnpm.bikerentalapp.bike.controller

import com.cnpm.bikerentalapp.bike.model.dto.BikeDTO
import com.cnpm.bikerentalapp.bike.model.httprequest.BikeCreateRequest
import com.cnpm.bikerentalapp.bike.model.httprequest.BikeUpdateRequest
import com.cnpm.bikerentalapp.bike.model.types.BikeType
import com.cnpm.bikerentalapp.bike.services.BikeServices
import com.cnpm.bikerentalapp.config.httpresponse.CRUDResponse
import com.cnpm.bikerentalapp.config.httpresponse.QueryResponse
import com.cnpm.bikerentalapp.station.model.entity.BikeStation
import com.cnpm.bikerentalapp.station.services.StationServices
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping("/api/bike")
class BikeController(
    private val bikeServices: BikeServices,
    private val stationServices: StationServices
) {

    @GetMapping("/")
    fun getAllBikes() : ResponseEntity<QueryResponse<Unit, BikeDTO>> {
        val bikes: List<BikeDTO> = bikeServices.getAllBikes()
        return ResponseEntity.ok()
            .header("Title", "BikeList")
            .body(QueryResponse("all", bikes.size, mapOf(), bikes))
    }

    @GetMapping("/id/{id}")
    fun getBikesByID(@Validated @PathVariable id: UUID) : ResponseEntity<QueryResponse<UUID, BikeDTO>> {
        val bike: BikeDTO = bikeServices.getBikeByID(id).mapBikeToDTO()
        return ResponseEntity.ok()
            .header("Title", "Bike")
            .body(QueryResponse("id", 1 ,mapOf("id" to id), listOf(bike)))
    }

    @GetMapping("/plate/{plate}")
    fun getBikesByPlate(@Validated @PathVariable plate: String) : ResponseEntity<QueryResponse<String, BikeDTO>> {
        val bike: BikeDTO = bikeServices.getBikeByPlate(plate)
        return ResponseEntity.ok()
            .header("Title", "Bike by plate $plate")
            .body(QueryResponse("plate", 1, mapOf("plate" to plate), listOf(bike)))
    }

    @GetMapping("/{type}")
    fun getBikesByType(@Validated @PathVariable type: BikeType) : ResponseEntity<QueryResponse<BikeType, BikeDTO>> {
        val bikes: List<BikeDTO> = bikeServices.getBikeByType(type)
        return ResponseEntity.ok()
            .header("Title", "BikeList by type $type")
            .body(QueryResponse("type", bikes.size, mapOf("type" to type), bikes))
    }

    @GetMapping("/available")
    fun getAvailableBikes() : ResponseEntity<QueryResponse<String, BikeDTO>> {
        val bikes: List<BikeDTO> = bikeServices.getAvailableBikes()
        return ResponseEntity.ok()
            .header("Title", "AvailableBikeList")
            .body(QueryResponse("status", bikes.size, mapOf("status" to "available"), bikes))
    }

    @GetMapping("/available/{type}")
    fun getAvailableBikesByType(@Validated @PathVariable type: BikeType) : ResponseEntity<QueryResponse<String, BikeDTO>> {
        val bikes: List<BikeDTO> = bikeServices.getAvailableBikesByType(type)
        return ResponseEntity.ok()
            .header("Title", "AvailableBikeList by type $type")
            .body(QueryResponse("status", bikes.size, mapOf("status" to "available", "type" to type.name), bikes))
    }

    @GetMapping("/count/{type}")
    fun countBikesByType(@Validated @PathVariable type: BikeType) : ResponseEntity<Map<String, String>> {
        val count: Int = bikeServices.countBikesByType(type)
        return ResponseEntity.ok()
            .header("Title", "BikeCount")
            .body(mapOf("queryBy" to "bike_count", "type" to type.name, "count" to count.toString()))
    }

    @PostMapping("/add")
    fun addBike(@Validated @RequestBody bike: BikeCreateRequest) : ResponseEntity<CRUDResponse<BikeDTO>> {
        val station: BikeStation? = if (bike.location != null) stationServices.getStationByID(bike.location) else null
        val newBike: BikeDTO = bikeServices.addBike(bike, station)
        return ResponseEntity.ok()
            .header("Title", "BikeAdded")
            .body(CRUDResponse("add", "success", target = newBike))
    }

    @PatchMapping("/update")
    fun updateBike(@Validated @RequestBody bike: BikeUpdateRequest) : ResponseEntity<CRUDResponse<BikeDTO>> {
        val id: UUID = bikeServices.getBikeIdByPlate(bike.plate)
        val capacity: Int? = if (bike.location != null) stationServices.getStationByID(bike.location).publicCapacity else null
        val updatedBike: BikeDTO = bikeServices.updateBike(id, bike, capacity)
        return ResponseEntity.ok()
            .header("Title", "BikeUpdated")
            .body(CRUDResponse("update", "success", target = updatedBike))
    }

    @DeleteMapping("/delete/{plate}")
    fun deleteBike(@Validated @PathVariable plate: String) : ResponseEntity<CRUDResponse<Unit>> {
        bikeServices.deleteBike(plate)
        return ResponseEntity.ok()
            .header("Title", "BikeDeleted")
            .body(CRUDResponse("delete", "success", target = null))
    }
}