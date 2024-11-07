package com.cnpm.bikerentalapp.config.jwt

import com.cnpm.bikerentalapp.user.principal.UserPrincipalAuthToken
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import org.springframework.util.StringUtils
import org.springframework.web.filter.OncePerRequestFilter
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
        extractTokenFromRequest(req)
            .map(jwtManager::decode)
            .map(jwtToPrincipal::convert)
            .map { UserPrincipalAuthToken(it) }
            .ifPresent {
                SecurityContextHolder.getContext().authentication = it
            }

        filterChain.doFilter(req, res)
    }
}