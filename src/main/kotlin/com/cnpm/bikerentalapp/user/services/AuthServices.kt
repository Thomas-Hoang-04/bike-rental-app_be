package com.cnpm.bikerentalapp.user.services

import com.cnpm.bikerentalapp.user.model.entity.UserCredential
import com.cnpm.bikerentalapp.user.principal.UserPrincipal
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Component

@Component
class AuthServices(
    private val userServices: UserServices
): UserDetailsService {
    override fun loadUserByUsername(username: String?): UserPrincipal {
        val user: UserCredential = userServices.getUserByUsername(username ?: "")
        return user.mapUserToPrincipal()
    }
}