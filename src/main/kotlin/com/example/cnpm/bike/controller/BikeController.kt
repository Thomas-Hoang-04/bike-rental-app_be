package com.example.cnpm.bike.controller

import com.example.cnpm.bike.exception.InvalidQuery
import com.example.cnpm.bike.model.dto.BikeCreateRequest
import com.example.cnpm.bike.model.dto.BikeDTO
import com.example.cnpm.bike.model.dto.BikeUpdateRequest
import com.example.cnpm.bike.model.httpresponse.BikeQueryResponse
import com.example.cnpm.bike.model.httpresponse.BikeUpdateResponse
import com.example.cnpm.bike.services.BikeServices
import jakarta.validation.Valid
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
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

    @GetMapping("/all")
    fun getAllBikes() : ResponseEntity<BikeQueryResponse> {
        val bikes: List<BikeDTO> = bikeServices.getAllBikes()
        return ResponseEntity.ok()
            .header("Title", "BikeList")
            .body(BikeQueryResponse("all", mapOf(), bikes))
    }

    @GetMapping("/id/{id}")
    fun getBikesByID(@Valid @PathVariable id: UUID) : ResponseEntity<BikeQueryResponse> {
        val bike = bikeServices.getBikeById(id)
        return ResponseEntity.ok()
            .header("Title", "Bike")
            .body(BikeQueryResponse("id", mapOf("id" to id.toString()), listOf(bike)))
    }

    @GetMapping("/plate/{plate}")
    fun getBikesByPlate(@Valid @PathVariable plate: String) : ResponseEntity<BikeQueryResponse> {
        val bike = bikeServices.getBikeByPlate(plate)
        return ResponseEntity.ok()
            .header("Title", "Bike by plate $plate")
            .body(BikeQueryResponse("plate", mapOf("plate" to plate), listOf(bike)))
    }

    @GetMapping("/{type}")
    fun getBikesByType(@Valid @PathVariable type: String) : ResponseEntity<BikeQueryResponse> {
        if (type != "electric" && type != "manual") throw InvalidQuery("Invalid bike type")
        val bikes: List<BikeDTO> = bikeServices.getBikeByType(type.uppercase())
        return ResponseEntity.ok()
            .header("Title", "BikeList by type $type")
            .body(BikeQueryResponse("type", mapOf("type" to type), bikes))
    }

    @GetMapping("/available")
    fun getAvailableBikes() : ResponseEntity<BikeQueryResponse> {
        val bikes: List<BikeDTO> = bikeServices.getAvailableBikes()
        return ResponseEntity.ok()
            .header("Title", "AvailableBikeList")
            .body(BikeQueryResponse("status", mapOf("status" to "available"), bikes))
    }

    @GetMapping("/available/{type}")
    fun getAvailableBikesByType(@Valid @PathVariable type: String) : ResponseEntity<BikeQueryResponse> {
        if (type != "electric" && type != "manual") throw InvalidQuery("Invalid bike type")
        val bikes: List<BikeDTO> = bikeServices.getAvailableBikesByType(type.uppercase())
        return ResponseEntity.ok()
            .header("Title", "AvailableBikeList by type $type")
            .body(BikeQueryResponse("status", mapOf("status" to "available", "type" to type), bikes))
    }

    @GetMapping("/count/{type}")
    fun countBikesByType(@Valid @PathVariable type: String) : ResponseEntity<Map<String, String>> {
        if (type != "electric" && type != "manual") throw InvalidQuery("Invalid bike type")
        val count: Int = bikeServices.countBikesByType(type.uppercase())
        return ResponseEntity.ok()
            .header("Title", "BikeCount")
            .body(mapOf("queryBy" to "bike_count", "type" to type, "count" to count.toString()))
    }

    @PostMapping("/add")
    fun addBike(@Valid @RequestBody bike: BikeCreateRequest) : ResponseEntity<BikeUpdateResponse> {
        val newBike: BikeDTO = bikeServices.addBike(bike)
        return ResponseEntity.ok()
            .header("Title", "BikeAdded")
            .body(BikeUpdateResponse("add", "success", newBike))
    }

    @PatchMapping("/update")
    fun updateBike(@Valid @RequestBody bike: BikeUpdateRequest) : ResponseEntity<BikeUpdateResponse> {
        val id: UUID = bikeServices.getBikeIdByPlate(bike.plate)
        val updatedBike: BikeDTO = bikeServices.updateBike(id, bike)
        return ResponseEntity.ok()
            .header("Title", "BikeUpdated")
            .body(BikeUpdateResponse("update", "success", updatedBike))
    }

    @DeleteMapping("/delete/{plate}")
    fun deleteBike(@Valid @PathVariable plate: String) : ResponseEntity<BikeUpdateResponse> {
        bikeServices.deleteBike(plate)
        return ResponseEntity.ok()
            .header("Title", "BikeDeleted")
            .body(BikeUpdateResponse("delete", "success", null))
    }
}