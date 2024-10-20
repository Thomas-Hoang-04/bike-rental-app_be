package com.example.cnpm.exception.bike

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(HttpStatus.NOT_FOUND)
data class BikeNotFoundException(override val message: String): RuntimeException()
