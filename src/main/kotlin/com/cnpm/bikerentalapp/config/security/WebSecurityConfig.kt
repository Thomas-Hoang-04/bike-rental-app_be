package com.cnpm.bikerentalapp.config.security

import com.cnpm.bikerentalapp.config.jwt.JWTAuthFilter
import com.cnpm.bikerentalapp.user.services.AuthServices
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.crypto.password.Pbkdf2PasswordEncoder
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter

@Configuration
@EnableWebSecurity
class WebSecurityConfig(
    private val jwtAuthFilter: JWTAuthFilter,
    private val authServices: AuthServices,
    private val pwdEncoder: Pbkdf2PasswordEncoder
) {

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
                        .requestMatchers("/api/auth/**").anonymous()
                        .requestMatchers("/api/otp/**").anonymous()
                        .requestMatchers("/api/**/add", "/api/user/**",
                            "/api/**/delete/**", "/api/**/update").hasRole("ADMIN")
                        .anyRequest().authenticated()
                }

        return http.build()
    }

    @Bean
    fun authManager(http: HttpSecurity): AuthenticationManager {
        val builder: AuthenticationManagerBuilder
            = http.getSharedObject(AuthenticationManagerBuilder::class.java)
        builder.userDetailsService(authServices)
            .passwordEncoder(pwdEncoder)
        return builder.build()
    }
}