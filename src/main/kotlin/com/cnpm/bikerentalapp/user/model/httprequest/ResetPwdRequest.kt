package com.cnpm.bikerentalapp.user.model.httprequest

import com.fasterxml.jackson.annotation.JsonProperty

data class ResetPwdRequest(
    val username: String,
    @JsonProperty("old_password")
    val oldPassword: String? = null,
    @JsonProperty("new_password")
    val newPassword: String,
)
