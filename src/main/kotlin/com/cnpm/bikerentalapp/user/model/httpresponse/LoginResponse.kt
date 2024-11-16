package com.cnpm.bikerentalapp.user.model.httpresponse

import com.cnpm.bikerentalapp.user.model.dto.UserDTO
import com.fasterxml.jackson.annotation.JsonProperty

data class LoginResponse(
    @JsonProperty("access_token")
    val accessToken: String,
    @JsonProperty("user")
    val targetUser: UserDTO
)
