package com.cnpm.bikerentalapp.config.security

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration

@Configuration
@ConfigurationProperties(prefix = "pwd")
class PwdEncoderKey {
    lateinit var secretKey: String
}