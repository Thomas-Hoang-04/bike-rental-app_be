package com.example.cnpm.station.controller

import com.example.cnpm.station.model.dto.*
import com.example.cnpm.station.model.httprequest.StationCreateRequest
import com.example.cnpm.station.model.httprequest.StationDeleteRequest
import com.example.cnpm.station.model.httprequest.StationUpdateRequest
import com.example.cnpm.station.model.httpresponse.StationQueryResponse
import com.example.cnpm.station.model.httpresponse.StationUpdateResponse
import com.example.cnpm.station.services.StationServices
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
import java.text.DecimalFormat
import java.util.UUID

@RestController
@RequestMapping("/api/station")
class StationController {

    @Autowired
    private lateinit var stationServices: StationServices

    @GetMapping("/all")
    fun getAllStations() : ResponseEntity<StationQueryResponse<Unit>> {
        val stations: List<StationDTO> = stationServices.getAllStations()
        return ResponseEntity.ok()
            .header("Title", "StationList")
            .body(StationQueryResponse("all", mapOf(), stations))
    }

    @GetMapping("/id/{id}")
    fun getStationByID(@Validated @PathVariable id: UUID) : ResponseEntity<StationQueryResponse<UUID>> {
        val station: StationDTO = stationServices.getStationByID(id)
        return ResponseEntity.ok()
            .header("Title", "Station")
            .body(StationQueryResponse("id", mapOf("id" to id), listOf(station)))
    }

    @GetMapping("/region/{regionID}/{regionNum}")
    fun getStationByRegionID(@Validated @PathVariable regionID: String, @Validated @PathVariable regionNum: Int) : ResponseEntity<StationQueryResponse<String>> {
        val station: StationDTO = stationServices.getStationByRegionID(regionID, regionNum)
        return ResponseEntity.ok()
            .header("Title", "Station by region $regionID${DecimalFormat("000").format(regionNum)}")
            .body(StationQueryResponse("region", mapOf("regionID" to regionID, "regionNum" to regionNum.toString()), listOf(station)))
    }

    @GetMapping("/region/{regionID}")
    fun getStationsByRegion(@Validated @PathVariable regionID: String) : ResponseEntity<StationQueryResponse<String>> {
        val stations: List<StationDTO> = stationServices.getStationsByRegion(regionID)
        return ResponseEntity.ok()
            .header("Title", "Stations by region $regionID")
            .body(StationQueryResponse("region", mapOf("regionID" to regionID), stations))
    }

    @GetMapping("/region")
    fun getStationsByCity(@Validated @RequestBody req: StationsByCity) : ResponseEntity<StationQueryResponse<String>> {
        val stations: List<StationDTO> = stationServices.getStationsByCity(req.city)
        return ResponseEntity.ok()
            .header("Title", "Stations by city")
            .body(StationQueryResponse("city", mapOf("city" to req.city), stations))
    }

    @GetMapping("/available")
    fun getAvailableStations() : ResponseEntity<StationQueryResponse<String>> {
        val stations: List<StationDTO> = stationServices.getAvailableStations()
        return ResponseEntity.ok()
            .header("Title", "AvailableStationList")
            .body(StationQueryResponse("status", mapOf("status" to "available"), stations))
    }

    @GetMapping("/nearby")
    fun getNearbyStations(@Validated @RequestBody req: NearbyStation) : ResponseEntity<StationQueryResponse<Double>> {
        val stations: List<StationDTO> = stationServices.getNearbyStations(req.latitude, req.longitude, req.radius)
        return ResponseEntity.ok()
            .header("Title", "NearbyStationList")
            .body(StationQueryResponse("nearby", mapOf("latitude" to req.latitude, "longitude" to req.longitude, "radius" to req.radius), stations))
    }

    @PostMapping("/add")
    fun addStation(@Validated @RequestBody req: StationCreateRequest) : ResponseEntity<StationUpdateResponse> {
        val station: StationDTO = stationServices.addStation(req)
        return ResponseEntity.ok()
            .header("Title", "Station added")
            .body(StationUpdateResponse("add", "success", station))
    }

    @PatchMapping("/update")
    fun updateStation(@Validated @RequestBody req: StationUpdateRequest) : ResponseEntity<StationUpdateResponse> {
        val station: StationDTO = stationServices.updateStation(req)
        return ResponseEntity.ok()
            .header("Title", "Station updated")
            .body(StationUpdateResponse("update", "success", station))
    }

    @DeleteMapping("/delete")
    fun deleteStation(@Validated @RequestBody req: StationDeleteRequest) : ResponseEntity<StationUpdateResponse> {
        stationServices.deleteStation(req)
        return ResponseEntity.ok()
            .header("Title", "Station deleted")
            .body(StationUpdateResponse("delete", "success", null))
    }
}