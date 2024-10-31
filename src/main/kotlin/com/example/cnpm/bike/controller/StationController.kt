package com.example.cnpm.bike.controller

import com.example.cnpm.bike.services.BikeServices
import com.example.cnpm.bike.services.BikeStationServices
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/station")
class StationController {

    @Autowired
    private lateinit var bikeServices: BikeServices

    @Autowired
    private lateinit var stationServices: BikeStationServices


}