package com.cnpm.bikerentalapp.config.jwt

import com.auth0.jwt.interfaces.Claim
import com.auth0.jwt.interfaces.DecodedJWT
import com.cnpm.bikerentalapp.user.principal.UserPrincipal
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.stereotype.Component
import java.util.*

@Component
class JWTToPrincipal {
    private fun extractAuthClaim(jwt: DecodedJWT)
        : List<SimpleGrantedAuthority> {
        val claim: Claim = jwt.getClaim("auth")
        if (claim.isNull || claim.isMissing) return emptyList()
        return claim.asList(SimpleGrantedAuthority::class.java)
    }

    fun convert(jwt: DecodedJWT): UserPrincipal {
        return UserPrincipal(
            UUID.fromString(jwt.subject),
            jwt.getClaim("username").asString(),
            extractAuthClaim(jwt)
        )
    }
}