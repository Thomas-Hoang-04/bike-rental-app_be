package com.cnpm.bikerentalapp

import com.cnpm.bikerentalapp.otp.config.TwilioConfig
import com.twilio.Twilio
import jakarta.annotation.PostConstruct
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.scheduling.annotation.EnableScheduling

@SpringBootApplication
@EnableScheduling
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
