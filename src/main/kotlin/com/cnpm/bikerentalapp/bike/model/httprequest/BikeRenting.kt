package com.cnpm.bikerentalapp.bike.model.httprequest

import com.cnpm.bikerentalapp.bike.model.types.BikeAction
import jakarta.validation.constraints.Max
import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotBlank

data class BikeRenting(
    @NotBlank(message = "Plate number is require")
    val plate: String,

    @NotBlank(message = "Current latitude is required")
    val latitude: Double,

    @NotBlank(message = "Current longitude is required")
    val longitude: Double,

    @NotBlank(message = "Action is required")
    val action: BikeAction,

    @Min(value = 0, message = "Arrival battery must be greater than or equal to 0")
    @Max(value = 100, message = "Arrival battery must be less than or equal to 100")
    val battery: Int?
)