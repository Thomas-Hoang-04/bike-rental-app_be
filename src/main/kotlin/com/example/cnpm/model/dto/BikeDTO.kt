package com.example.cnpm.model.dto

import com.example.cnpm.model.BikeStatus
import com.example.cnpm.model.BikeType
import java.util.UUID

data class BikeDTO(
    val id: UUID,
    val plate: String,
    val type: BikeType,
    val battery: Int,
    val status: BikeStatus,
)
