package com.example.cnpm

import com.example.cnpm.otp.config.TwilioConfig
import com.twilio.Twilio
import jakarta.annotation.PostConstruct
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class CnpmApplication {
    @Autowired
    private lateinit var twilioConfig: TwilioConfig

    @PostConstruct
    fun setup() {
        Twilio.init(twilioConfig.accountSid, twilioConfig.authToken)
    }
}

fun main(args: Array<String>) {
    runApplication<CnpmApplication>(*args)
}
