package com.cnpm.bikerentalapp

import com.cnpm.bikerentalapp.otp.config.TwilioConfig
import com.twilio.Twilio
import jakarta.annotation.PostConstruct
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class BikeRentalApp {
    @Autowired
    private lateinit var twilioConfig: TwilioConfig

    @PostConstruct
    fun setup() {
        Twilio.init(twilioConfig.accountSid, twilioConfig.authToken)
    }
}

fun main(args: Array<String>) {
    runApplication<BikeRentalApp>(*args)
}
