package com.cnpm.bikerentalapp.user.model.httprequest

import com.cnpm.bikerentalapp.user.model.types.UserRole
import com.cnpm.bikerentalapp.user.model.dto.UserDetailsDTO

data class UserCreateRequest(
    val username: String,
    val password: String,
    val role: UserRole?,
    val details: UserDetailsDTO
)
