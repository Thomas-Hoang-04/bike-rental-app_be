package com.cnpm.bikerentalapp.user.model.httpresponse

import com.fasterxml.jackson.annotation.JsonProperty

data class LoginResponse(
    @JsonProperty("access_token")
    val accessToken: String,
)
