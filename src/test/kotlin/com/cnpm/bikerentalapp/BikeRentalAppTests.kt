package com.cnpm.bikerentalapp

import com.cnpm.bikerentalapp.config.security.PwdEncoderKey
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class BikeRentalAppTests {

    @Autowired
    private lateinit var keystore: PwdEncoderKey

    @Test
    fun getSecretKey() {
        println(keystore.secretKey)
        assert(keystore.secretKey.isNotEmpty())
    }

}
