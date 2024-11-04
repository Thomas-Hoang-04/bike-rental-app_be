package com.cnpm.bikerentalapp.bike.model.dto

import com.cnpm.bikerentalapp.bike.model.types.BikeStatus
import com.cnpm.bikerentalapp.bike.model.types.BikeType
import java.util.UUID

data class BikeDTO(
    val id: UUID,
    val plate: String,
    val type: BikeType,
    val battery: Int,
    val status: BikeStatus,
    val location: UUID?,
)
