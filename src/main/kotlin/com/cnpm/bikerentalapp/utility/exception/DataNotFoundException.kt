package com.cnpm.bikerentalapp.utility.exception

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(HttpStatus.NOT_FOUND)
data class DataNotFoundException(override val message: String): RuntimeException()
