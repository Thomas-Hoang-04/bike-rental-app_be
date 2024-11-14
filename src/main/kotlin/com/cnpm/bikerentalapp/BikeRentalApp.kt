package com.cnpm.bikerentalapp

import com.cnpm.bikerentalapp.otp.config.TwilioConfig
import com.twilio.Twilio
import jakarta.annotation.PostConstruct
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class BikeRentalApp(
    private val twilioConfig: TwilioConfig
) {

    @PostConstruct
    fun setup() {
        Twilio.init(twilioConfig.accountSid, twilioConfig.authToken)
    }
}

fun main(args: Array<String>) {
    runApplication<BikeRentalApp>(*args)
}
