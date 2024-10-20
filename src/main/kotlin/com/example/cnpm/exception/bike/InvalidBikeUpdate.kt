package com.example.cnpm.exception.bike

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(HttpStatus.BAD_REQUEST)
data class InvalidBikeUpdate(override val message: String): IllegalArgumentException()
