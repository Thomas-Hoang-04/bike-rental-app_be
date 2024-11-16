package com.cnpm.bikerentalapp.station.controller

import com.cnpm.bikerentalapp.config.httpresponse.CRUDResponse
import com.cnpm.bikerentalapp.config.httpresponse.QueryResponse
import com.cnpm.bikerentalapp.station.model.dto.NearbyStation
import com.cnpm.bikerentalapp.station.model.dto.StationDTO
import com.cnpm.bikerentalapp.station.model.httprequest.StationCreateRequest
import com.cnpm.bikerentalapp.station.model.httprequest.StationDeleteRequest
import com.cnpm.bikerentalapp.station.model.httprequest.StationUpdateRequest
import com.cnpm.bikerentalapp.station.services.StationServices
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import java.text.DecimalFormat
import java.util.*

@RestController
@RequestMapping("/api/station")
class StationController(
    private val stationServices: StationServices
) {

    @GetMapping("/")
    fun getAllStations() : ResponseEntity<QueryResponse<Unit, StationDTO>> {
        val stations: List<StationDTO> = stationServices.getAllStations()
        return ResponseEntity.ok()
            .header("Title", "StationList")
            .body(QueryResponse("all", stations.size, mapOf() ,stations))
    }

    @GetMapping("/id/{id}")
    fun getStationByID(@Validated @PathVariable id: UUID) : ResponseEntity<QueryResponse<UUID, StationDTO>> {
        val station: StationDTO = stationServices.getStationByID(id).mapStationToDTO()
        return ResponseEntity.ok()
            .header("Title", "Station")
            .body(QueryResponse("id", 1, mapOf("id" to id), listOf(station)))
    }

    @GetMapping("/region")
    fun getStationsByCity(@Validated @RequestParam city: String) : ResponseEntity<QueryResponse<String, StationDTO>> {
        val stations: List<StationDTO> = stationServices.getStationsByCity(city)
        return ResponseEntity.ok()
            .header("Title", "Stations by city")
            .body(QueryResponse("city", stations.size, mapOf("city" to city), stations))
    }

    @GetMapping("/region/{regionID}")
    fun getStationsByRegion(@Validated @PathVariable regionID: String) : ResponseEntity<QueryResponse<String, StationDTO>> {
        val stations: List<StationDTO> = stationServices.getStationsByRegion(regionID)
        return ResponseEntity.ok()
            .header("Title", "Stations by region $regionID")
            .body(QueryResponse("region", stations.size, mapOf("regionID" to regionID), stations))
    }

    @GetMapping("/region/{regionID}/{regionNum}")
    fun getStationByRegionID(@Validated @PathVariable regionID: String, @Validated @PathVariable regionNum: Int) : ResponseEntity<QueryResponse<String, StationDTO>> {
        val station: StationDTO = stationServices.getStationByRegionID(regionID, regionNum)
        return ResponseEntity.ok()
            .header("Title", "Station by region $regionID${DecimalFormat("000").format(regionNum)}")
            .body(QueryResponse("region", 1, mapOf("regionID" to regionID, "regionNum" to regionNum.toString()), listOf(station)))
    }

    @GetMapping("/available")
    fun getAvailableStations() : ResponseEntity<QueryResponse<String, StationDTO>> {
        val stations: List<StationDTO> = stationServices.getAvailableStations()
        return ResponseEntity.ok()
            .header("Title", "AvailableStationList")
            .body(QueryResponse("status", stations.size, mapOf("status" to "available"), stations))
    }

    @GetMapping("/available/{regionID}")
    fun getAvailableStationsByRegion(@Validated @PathVariable regionID: String) : ResponseEntity<QueryResponse<String, StationDTO>> {
        val stations: List<StationDTO> = stationServices.getAvailableStationsByRegion(regionID)
        return ResponseEntity.ok()
            .header("Title", "AvailableStationList by region $regionID")
            .body(QueryResponse("status", stations.size, mapOf("status" to "available", "regionID" to regionID), stations))
    }

    @GetMapping("/nearby")
    fun getNearbyStations(@Validated @RequestBody req: NearbyStation) : ResponseEntity<QueryResponse<Double, StationDTO>> {
        val stations: List<StationDTO> = stationServices.getNearbyStations(req.latitude, req.longitude, req.radius)
        return ResponseEntity.ok()
            .header("Title", "NearbyStationList")
            .body(QueryResponse("nearby", stations.size, mapOf("latitude" to req.latitude, "longitude" to req.longitude, "radius" to req.radius), stations))
    }

    @PostMapping("/add")
    fun addStation(@Validated @RequestBody req: StationCreateRequest) : ResponseEntity<CRUDResponse<StationDTO>> {
        val station: StationDTO = stationServices.addStation(req)
        return ResponseEntity.ok()
            .header("Title", "Station added")
            .body(CRUDResponse("add", "success", target = station))
    }

    @PatchMapping("/update")
    fun updateStation(@Validated @RequestBody req: StationUpdateRequest) : ResponseEntity<CRUDResponse<StationDTO>> {
        val station: StationDTO = stationServices.updateStation(req)
        return ResponseEntity.ok()
            .header("Title", "Station updated")
            .body(CRUDResponse("update", "success", target = station))
    }

    @DeleteMapping("/delete")
    fun deleteStation(@Validated @RequestBody req: StationDeleteRequest) : ResponseEntity<CRUDResponse<Unit>> {
        stationServices.deleteStation(req)
        return ResponseEntity.ok()
            .header("Title", "Station deleted")
            .body(CRUDResponse("delete", "success", target = null))
    }
}