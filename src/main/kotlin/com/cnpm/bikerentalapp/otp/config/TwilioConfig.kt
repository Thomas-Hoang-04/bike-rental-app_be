package com.cnpm.bikerentalapp.otp.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration
import org.springframework.stereotype.Component

@Configuration
@Component
@ConfigurationProperties(prefix = "twilio")
class TwilioConfig {
    lateinit var accountSid: String
    lateinit var authToken: String
    lateinit var phoneNumber: String
}