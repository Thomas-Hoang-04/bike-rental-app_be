package com.cnpm.bikerentalapp.config.jwt

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.auth0.jwt.interfaces.DecodedJWT
import com.cnpm.bikerentalapp.config.security.RSAKeyProperties
import org.springframework.stereotype.Component
import java.time.Duration
import java.time.Instant
import java.util.*

@Component
class JWTManager(
    private val rsaKeys: RSAKeyProperties
) {

    fun issue(id: UUID, username: String, roles: List<String>): String {
        return JWT.create()
            .withSubject(id.toString())
            .withExpiresAt(Instant.now().plus(Duration.ofDays(1)))
            .withClaim("username", username)
            .withClaim("auth", roles)
            .sign(Algorithm.RSA512(rsaKeys.publicKey, rsaKeys.privateKey))
    }

    fun decode(token: String): DecodedJWT
        = JWT.require(Algorithm.RSA512(rsaKeys.publicKey, rsaKeys.privateKey))
            .build()
            .verify(token)
}