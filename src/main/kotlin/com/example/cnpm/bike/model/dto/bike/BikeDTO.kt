package com.example.cnpm.bike.model.dto.bike

import com.example.cnpm.bike.model.types.BikeStatus
import com.example.cnpm.bike.model.types.BikeType
import java.util.UUID

data class BikeDTO(
    val id: UUID,
    val plate: String,
    val type: BikeType,
    val battery: Int,
    val status: BikeStatus,
    val location: String?,
)
