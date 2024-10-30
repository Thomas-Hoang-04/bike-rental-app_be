package com.example.cnpm.bike.exception

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(HttpStatus.NOT_FOUND, reason = "Bike not found")
data class BikeNotFoundException(override val message: String): RuntimeException()
