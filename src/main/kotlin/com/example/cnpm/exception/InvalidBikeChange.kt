package com.example.cnpm.exception

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(HttpStatus.BAD_REQUEST)
data class InvalidBikeChange(override val message: String): IllegalArgumentException()
