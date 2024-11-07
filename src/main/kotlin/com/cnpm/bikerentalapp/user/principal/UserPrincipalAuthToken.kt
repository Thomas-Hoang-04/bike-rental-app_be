package com.cnpm.bikerentalapp.user.principal

import org.springframework.security.authentication.AbstractAuthenticationToken

class UserPrincipalAuthToken(private val principal: UserPrincipal)
    : AbstractAuthenticationToken(principal.authorities) {

        init {
            isAuthenticated = true
        }

        override fun getCredentials() {
            TODO("Not yet implemented")
        }

        override fun getPrincipal(): UserPrincipal = principal
}