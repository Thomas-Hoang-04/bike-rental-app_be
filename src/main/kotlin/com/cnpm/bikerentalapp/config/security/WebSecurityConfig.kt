package com.cnpm.bikerentalapp.config.security

import com.cnpm.bikerentalapp.config.jwt.JWTAuthFilter
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter

@Configuration
@EnableWebSecurity
class WebSecurityConfig {

    @Autowired
    private lateinit var jwtAuthFilter: JWTAuthFilter

    @Bean
    @Throws(Exception::class)
    fun applicationSecurity(http: HttpSecurity): SecurityFilterChain {
        http.addFilterBefore(jwtAuthFilter,
            UsernamePasswordAuthenticationFilter::class.java)

        http
            .cors { it.disable() }
            .csrf { it.disable() }
            .sessionManagement {
                it.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            }
            .formLogin { it.disable() }
            .securityMatcher("/**")
                .authorizeHttpRequests {
                    it
                        .requestMatchers("/actuator/**").permitAll()
                        .requestMatchers("/api/auth/**").permitAll()
                        .anyRequest().authenticated()
                }

        return http.build()
    }
}