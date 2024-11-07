package com.cnpm.bikerentalapp.config.jwt

import com.auth0.jwt.exceptions.JWTVerificationException
import com.cnpm.bikerentalapp.exception.model.ErrorResponse
import com.cnpm.bikerentalapp.user.principal.UserPrincipalAuthToken
import com.fasterxml.jackson.databind.ObjectMapper
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import org.springframework.util.StringUtils
import org.springframework.web.filter.OncePerRequestFilter
import java.time.LocalDateTime
import java.util.Optional

@Component
class JWTAuthFilter: OncePerRequestFilter() {

    @Autowired
    private lateinit var jwtManager: JWTManager

    @Autowired
    private lateinit var jwtToPrincipal: JWTToPrincipal

    private fun extractTokenFromRequest(req: HttpServletRequest): Optional<String> {
        val token: String? = req.getHeader("Authorization")
        return if (StringUtils.hasText(token) && token!!.startsWith("Bearer "))
            Optional.of(token.substring(7)) else Optional.empty()
    }

    override fun doFilterInternal(
        req: HttpServletRequest,
        res: HttpServletResponse,
        filterChain: FilterChain
    ) {
        try {
            extractTokenFromRequest(req)
                .map(jwtManager::decode)
                .map(jwtToPrincipal::convert)
                .map { UserPrincipalAuthToken(it) }
                .ifPresent {
                    SecurityContextHolder.getContext().authentication = it
                }

            filterChain.doFilter(req, res)
        } catch (e: JWTVerificationException) {
            val errorRes = ErrorResponse(
                LocalDateTime.now().toString(),
                HttpServletResponse.SC_UNAUTHORIZED,
                "Unauthorized",
                e.javaClass,
                e.message ?: "Invalid JWT",
                req.requestURI
            )
            res.status = HttpServletResponse.SC_UNAUTHORIZED
            res.contentType = "application/json"
            res.characterEncoding = "UTF-8"
            res.writer.write(ObjectMapper()
                .writeValueAsString(errorRes)
            )
        }
    }
}