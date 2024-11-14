package com.cnpm.bikerentalapp.config.exception.model

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(HttpStatus.BAD_REQUEST)
data class InvalidQuery(override val message: String): IllegalArgumentException()
