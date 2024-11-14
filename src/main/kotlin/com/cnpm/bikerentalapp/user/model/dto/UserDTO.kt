package com.cnpm.bikerentalapp.user.model.dto

import com.cnpm.bikerentalapp.user.model.types.UserRole
import java.util.*

data class UserDTO(
    val id: UUID,
    val username: String,
    val role: UserRole,
    val details: UserDetailsDTO
)
