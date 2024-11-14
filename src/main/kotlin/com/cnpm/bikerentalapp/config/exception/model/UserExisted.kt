package com.cnpm.bikerentalapp.config.exception.model

data class UserExisted(override val message: String) : IllegalCallerException()
