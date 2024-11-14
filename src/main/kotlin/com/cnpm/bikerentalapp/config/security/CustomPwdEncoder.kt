package com.cnpm.bikerentalapp.config.security


import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

import org.springframework.security.crypto.password.Pbkdf2PasswordEncoder

@Configuration
class CustomPwdEncoder(
    private val keystore: PwdEncoderKey
) {

    @Bean
    fun passwordEncoder(): Pbkdf2PasswordEncoder {
        val hmacSHA512 = Pbkdf2PasswordEncoder.SecretKeyFactoryAlgorithm.PBKDF2WithHmacSHA512
        return Pbkdf2PasswordEncoder(keystore.secretKey, 16, 300000, hmacSHA512)
    }
}