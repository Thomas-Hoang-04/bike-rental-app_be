package com.example.cnpm.model.bike.dto

import com.example.cnpm.model.bike.types.BikeStatus
import com.example.cnpm.model.bike.types.BikeType
import jakarta.validation.constraints.Max
import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotBlank

data class BikeCreateRequest(
    @NotBlank(message = "Plate number is required")
    val plate: String,

    @NotBlank(message = "Bike type is required")
    val type: BikeType,

    @Min(0, message = "Battery must be between 0 and 100")
    @Max(100, message = "Battery must be between 0 and 100")
    val battery: Int?,
    val status: BikeStatus?
)
