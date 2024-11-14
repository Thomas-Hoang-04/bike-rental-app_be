package com.cnpm.bikerentalapp.user.model.httprequest

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken

data class LoginRequest(
    val username: String,
    val password: String
) {
    fun mapToAuthToken(): UsernamePasswordAuthenticationToken
        = UsernamePasswordAuthenticationToken(username, password)
}
