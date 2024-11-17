package com.cnpm.bikerentalapp.config.exception.model

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(HttpStatus.CONFLICT)
data class UserExisted(override val message: String) : IllegalCallerException()
