package com.example.cnpm.model.dto

import com.example.cnpm.model.BikeStatus
import com.example.cnpm.model.BikeType
import jakarta.validation.constraints.Max
import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotBlank

data class BikeUpdateRequest(
    @NotBlank(message = "Plate number is required")
    val plate: String,
    val type: BikeType?,
    val status: BikeStatus?,

    @Min(0, message = "Battery must be between 0 and 100")
    @Max(100, message = "Battery must be between 0 and 100")
    val battery: Int?
)
