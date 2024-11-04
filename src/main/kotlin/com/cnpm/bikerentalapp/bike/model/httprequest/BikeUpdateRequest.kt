package com.cnpm.bikerentalapp.bike.model.httprequest

import com.cnpm.bikerentalapp.bike.model.types.BikeStatus
import com.cnpm.bikerentalapp.bike.model.types.BikeType
import com.fasterxml.jackson.annotation.JsonProperty
import jakarta.validation.constraints.Max
import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotBlank
import java.util.UUID

data class BikeUpdateRequest(
    @NotBlank(message = "Plate number is required")
    val plate: String,

    @JsonProperty("new_plate")
    val newPlate: String?,
    val type: BikeType?,
    val status: BikeStatus?,

    @Min(0, message = "Battery must be between 0 and 100")
    @Max(100, message = "Battery must be between 0 and 100")
    val battery: Int?,

    val location: UUID?
)
