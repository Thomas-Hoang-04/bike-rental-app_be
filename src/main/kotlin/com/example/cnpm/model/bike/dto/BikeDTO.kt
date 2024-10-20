package com.example.cnpm.model.bike.dto

import com.example.cnpm.model.bike.types.BikeStatus
import com.example.cnpm.model.bike.types.BikeType
import java.util.UUID

data class BikeDTO(
    val id: UUID,
    val plate: String,
    val type: BikeType,
    val battery: Int,
    val status: BikeStatus,
)
