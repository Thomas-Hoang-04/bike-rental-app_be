package com.cnpm.bikerentalapp.config

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import java.time.Instant
import java.time.Duration


@Component
class JWTIssuer {

    @Autowired
    private lateinit var prop: JWTProperties;

    fun issue(id: Long, username: String, roles: List<String>): String {
        return JWT.create()
            .withSubject(id.toString())
            .withExpiresAt(Instant.now().plus(Duration.ofDays(1)))
            .withClaim("username", username)
            .withClaim("auth", roles)
            .sign(Algorithm.HMAC512(prop.secret))
    }
}