package com.cnpm.bikerentalapp.user.principal

import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import java.util.UUID

class UserPrincipal(
    private val userID: UUID,
    private val username: String,
    private val auth: Collection<SimpleGrantedAuthority>
    ): UserDetails {

    override fun getAuthorities(): Collection<SimpleGrantedAuthority> = auth

    override fun getPassword(): String {
        TODO("Not yet implemented")
    }

    override fun getUsername(): String = username

}