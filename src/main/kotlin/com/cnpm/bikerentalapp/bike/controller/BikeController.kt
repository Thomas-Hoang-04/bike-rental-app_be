package com.cnpm.bikerentalapp.bike.controller

import com.cnpm.bikerentalapp.bike.model.httprequest.BikeCreateRequest
import com.cnpm.bikerentalapp.bike.model.dto.BikeDTO
import com.cnpm.bikerentalapp.bike.model.httprequest.BikeUpdateRequest
import com.cnpm.bikerentalapp.bike.model.httpresponse.BikeQueryResponse
import com.cnpm.bikerentalapp.bike.model.httpresponse.BikeUpdateResponse
import com.cnpm.bikerentalapp.bike.model.types.BikeType
import com.cnpm.bikerentalapp.bike.services.BikeServices
import com.cnpm.bikerentalapp.station.services.StationServices
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController


import java.util.UUID

@RestController
@RequestMapping("/api/bike")
class BikeController {

    @Autowired
    private lateinit var bikeServices: BikeServices

    @Autowired
    private lateinit var stationServices: StationServices

    @GetMapping("/all")
    fun getAllBikes() : ResponseEntity<BikeQueryResponse<Unit>> {
        val bikes: List<BikeDTO> = bikeServices.getAllBikes()
        return ResponseEntity.ok()
            .header("Title", "BikeList")
            .body(BikeQueryResponse("all", mapOf(), bikes))
    }

    @GetMapping("/id/{id}")
    fun getBikesByID(@Validated @PathVariable id: UUID) : ResponseEntity<BikeQueryResponse<UUID>> {
        val bike: BikeDTO = bikeServices.getBikeById(id)
        return ResponseEntity.ok()
            .header("Title", "Bike")
            .body(BikeQueryResponse("id", mapOf("id" to id), listOf(bike)))
    }

    @GetMapping("/plate/{plate}")
    fun getBikesByPlate(@Validated @PathVariable plate: String) : ResponseEntity<BikeQueryResponse<String>> {
        val bike: BikeDTO = bikeServices.getBikeByPlate(plate)
        return ResponseEntity.ok()
            .header("Title", "Bike by plate $plate")
            .body(BikeQueryResponse("plate", mapOf("plate" to plate), listOf(bike)))
    }

    @GetMapping("/{type}")
    fun getBikesByType(@Validated @PathVariable type: BikeType) : ResponseEntity<BikeQueryResponse<BikeType>> {
        val bikes: List<BikeDTO> = bikeServices.getBikeByType(type)
        return ResponseEntity.ok()
            .header("Title", "BikeList by type $type")
            .body(BikeQueryResponse("type", mapOf("type" to type), bikes))
    }

    @GetMapping("/available")
    fun getAvailableBikes() : ResponseEntity<BikeQueryResponse<String>> {
        val bikes: List<BikeDTO> = bikeServices.getAvailableBikes()
        return ResponseEntity.ok()
            .header("Title", "AvailableBikeList")
            .body(BikeQueryResponse("status", mapOf("status" to "available"), bikes))
    }

    @GetMapping("/available/{type}")
    fun getAvailableBikesByType(@Validated @PathVariable type: BikeType) : ResponseEntity<BikeQueryResponse<String>> {
        val bikes: List<BikeDTO> = bikeServices.getAvailableBikesByType(type)
        return ResponseEntity.ok()
            .header("Title", "AvailableBikeList by type $type")
            .body(BikeQueryResponse("status", mapOf("status" to "available", "type" to type.name), bikes))
    }

    @GetMapping("/count/{type}")
    fun countBikesByType(@Validated @PathVariable type: BikeType) : ResponseEntity<Map<String, String>> {
        val count: Int = bikeServices.countBikesByType(type)
        return ResponseEntity.ok()
            .header("Title", "BikeCount")
            .body(mapOf("queryBy" to "bike_count", "type" to type.name, "count" to count.toString()))
    }

    @PostMapping("/add")
    fun addBike(@Validated @RequestBody bike: BikeCreateRequest) : ResponseEntity<BikeUpdateResponse> {
        val newBike: BikeDTO = bikeServices.addBike(bike)
        return ResponseEntity.ok()
            .header("Title", "BikeAdded")
            .body(BikeUpdateResponse("add", "success", newBike))
    }

    @PatchMapping("/update")
    fun updateBike(@Validated @RequestBody bike: BikeUpdateRequest) : ResponseEntity<BikeUpdateResponse> {
        val id: UUID = bikeServices.getBikeIdByPlate(bike.plate)
        val capacity: Int? = if (bike.location != null) stationServices.getStationByID(bike.location).capacity else null
        val updatedBike: BikeDTO = bikeServices.updateBike(id, bike, capacity)
        return ResponseEntity.ok()
            .header("Title", "BikeUpdated")
            .body(BikeUpdateResponse("update", "success", updatedBike))
    }

    @DeleteMapping("/delete/{plate}")
    fun deleteBike(@Validated @PathVariable plate: String) : ResponseEntity<BikeUpdateResponse> {
        bikeServices.deleteBike(plate)
        return ResponseEntity.ok()
            .header("Title", "BikeDeleted")
            .body(BikeUpdateResponse("delete", "success", null))
    }
}