package com.cnpm.bikerentalapp.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration

@Configuration
@ConfigurationProperties("security.jwt")
class JWTProperties {
    lateinit var secret: String
}