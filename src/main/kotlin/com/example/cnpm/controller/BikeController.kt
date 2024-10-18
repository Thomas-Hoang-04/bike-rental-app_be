package com.example.cnpm.controller

import com.example.cnpm.model.dto.BikeCreateRequest
import com.example.cnpm.model.dto.BikeDTO
import com.example.cnpm.model.dto.BikeUpdateRequest
import com.example.cnpm.services.BikeServices
import jakarta.validation.Valid
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
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
    fun getAllBikes() : ResponseEntity<List<BikeDTO>> {
        val bikes: List<BikeDTO> = bikeServices.getAllBikes()
        return ResponseEntity.ok()
            .header("Title", "BikeList")
            .body(bikes)
    }

    @GetMapping("/id/{id}")
    fun getBikesByID(@PathVariable id: UUID) : ResponseEntity<BikeDTO> {
        val bike = bikeServices.getBikeById(id)
        return ResponseEntity.ok()
            .header("Title", "Bike")
            .body(bike)
    }

    @GetMapping("/plate/{plate}")
    fun getBikesByPlate(@PathVariable plate: String) : ResponseEntity<BikeDTO> {
        val bike = bikeServices.getBikeByPlate(plate)
        return ResponseEntity.ok()
            .header("Title", "Bike")
            .body(bike)
    }

    @PostMapping("/add")
    fun addBike(@Valid @RequestBody bike: BikeCreateRequest) : ResponseEntity<BikeDTO> {
        val newBike: BikeDTO = bikeServices.addBike(bike)
        return ResponseEntity.ok()
            .header("Title", "BikeAdded")
            .body(newBike)
    }

    @PatchMapping("/update/{id}")
    fun updateBike(@Valid @RequestBody bike: BikeUpdateRequest) : ResponseEntity<BikeDTO> {
        val id: UUID = bikeServices.getBikeIdByPlate(bike.plate)
        val updatedBike: BikeDTO = bikeServices.updateBike(id, bike)
        return ResponseEntity.ok()
            .header("Title", "BikeUpdated")
            .body(updatedBike)
    }

}