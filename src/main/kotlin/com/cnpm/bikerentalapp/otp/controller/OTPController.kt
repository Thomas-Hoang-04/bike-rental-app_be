package com.cnpm.bikerentalapp.otp.controller

import com.cnpm.bikerentalapp.otp.model.OTPRequest
import com.cnpm.bikerentalapp.otp.model.OTPResponse
import com.cnpm.bikerentalapp.otp.model.OTPStatus
import com.cnpm.bikerentalapp.otp.model.OTPValidationRequest
import com.cnpm.bikerentalapp.otp.services.OTPService
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/otp")
class OTPController(
    private val otpService: OTPService
) {

    @PostMapping("/send")
    fun sendOTP(@Valid @RequestBody req: OTPRequest): ResponseEntity<OTPResponse> {
        val res: OTPResponse = otpService.sendOTP(req)
        return when (res.status) {
            OTPStatus.SUCCESS -> ResponseEntity.ok().body(res)
            OTPStatus.INVALID -> ResponseEntity.badRequest().body(res)
            else -> ResponseEntity.internalServerError().body(res)
        }
    }

    @PostMapping("/verify")
    fun verifyOTP(@Valid @RequestBody req: OTPValidationRequest): ResponseEntity<OTPResponse> {
        val res: OTPResponse = otpService.verifyOTP(req)
        return when (res.status) {
            OTPStatus.SUCCESS -> ResponseEntity.ok().body(res)
            OTPStatus.INVALID -> ResponseEntity.badRequest().body(res)
            OTPStatus.EXPIRED -> ResponseEntity.status(HttpStatus.GONE).body(res)
            else -> ResponseEntity.internalServerError().body(res)
        }
    }
}