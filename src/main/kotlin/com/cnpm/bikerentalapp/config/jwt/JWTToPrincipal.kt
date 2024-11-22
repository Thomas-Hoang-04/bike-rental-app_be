package com.cnpm.bikerentalapp.config.jwt

import com.auth0.jwt.interfaces.Claim
import com.auth0.jwt.interfaces.DecodedJWT
import com.cnpm.bikerentalapp.config.exception.model.DataNotFoundException
import com.cnpm.bikerentalapp.user.model.entity.UserCredential
import com.cnpm.bikerentalapp.user.principal.UserPrincipal
import com.cnpm.bikerentalapp.user.services.UserServices
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.stereotype.Component
import java.util.*

@Component
class JWTToPrincipal(
    private val userServices: UserServices
) {
    private fun extractAuthClaim(jwt: DecodedJWT)
        : List<SimpleGrantedAuthority> {
        val claim: Claim = jwt.getClaim("auth")
        if (claim.isNull || claim.isMissing) return emptyList()
        return claim.asList(SimpleGrantedAuthority::class.java)
    }

    fun convert(jwt: DecodedJWT): UserPrincipal {
        try {
            val user: UserCredential = userServices.getUserByUsername(
                jwt.getClaim("username").asString() ?: "")
            assert(user.mapUserToDTO().id == UUID.fromString(jwt.subject))
            return UserPrincipal(
                UUID.fromString(jwt.subject),
                jwt.getClaim("username").asString(),
                extractAuthClaim(jwt)
            )
        } catch (e: DataNotFoundException) {
            throw BadCredentialsException(e.message)
        } catch (_: AssertionError) {
            throw BadCredentialsException("Username ID from JWT is invalid")
        }
    }
}