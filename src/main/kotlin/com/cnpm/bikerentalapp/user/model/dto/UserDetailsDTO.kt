package com.cnpm.bikerentalapp.user.model.dto

import com.fasterxml.jackson.annotation.JsonProperty

data class UserDetailsDTO(
    val name: String,
    @JsonProperty("phone_num")
    val phoneNum: String,
    val email: String,
    val dob: String,
    val balance: Int
)
