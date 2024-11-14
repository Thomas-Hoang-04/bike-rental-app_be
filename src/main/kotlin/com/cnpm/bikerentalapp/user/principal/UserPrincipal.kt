package com.cnpm.bikerentalapp.user.principal

import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import java.util.*

class UserPrincipal(
    private val userID: UUID,
    private val username: String,
    private val auth: Collection<SimpleGrantedAuthority>
    ): UserDetails {

    private lateinit var password: String

    constructor(userID: UUID,
                username: String,
                password: String,
                auth: Collection<SimpleGrantedAuthority>): this(userID, username, auth) {
         this.password = password
    }

    val id: UUID
        get() = userID

    override fun getAuthorities(): Collection<SimpleGrantedAuthority> = auth

    override fun getPassword(): String = password

    override fun getUsername(): String = username

}