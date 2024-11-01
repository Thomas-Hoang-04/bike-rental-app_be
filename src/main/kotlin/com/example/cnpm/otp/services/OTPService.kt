package com.example.cnpm.otp.services

import com.example.cnpm.otp.config.TwilioConfig
import com.example.cnpm.otp.model.OTPRequest
import com.example.cnpm.otp.model.OTPResponse
import com.example.cnpm.otp.model.OTPStatus
import com.example.cnpm.otp.model.OTPValidationRequest
import com.twilio.rest.api.v2010.account.Message

import com.twilio.type.PhoneNumber
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.text.DecimalFormat
import kotlin.random.Random

@Service
class OTPService {
    @Autowired
    private lateinit var twilioConfig: TwilioConfig
    private val otpMap: HashMap<String, Pair<String, Long>> = HashMap()

    private fun generateOTP(): String = DecimalFormat("00000").format(Random.nextInt(100000))

    private fun generateMessage(otp: String): String = "Mã xác thực của bạn là: $otp. Mã xác thực này sẽ hết hạn sau 5 phút."

    fun sendOTP(req: OTPRequest): OTPResponse {
        try {
            if (otpMap[req.userName] != null && System.currentTimeMillis() - otpMap[req.userName]!!.second < 60000) {
                return OTPResponse(OTPStatus.INVALID, "Please wait at least 60 seconds before requesting another OTP")
            }
            val tar = PhoneNumber(req.phoneNumber)
            val from = PhoneNumber(twilioConfig.phoneNumber)
            val otp: String = generateOTP()
            val message: String = generateMessage(otp)
            Message.creator(tar, from, message).create()
            otpMap[req.userName] = Pair(otp, System.currentTimeMillis())
            return OTPResponse(OTPStatus.SUCCESS, "OTP sent successfully")
        } catch (e: Exception) {
            e.printStackTrace()
            return OTPResponse(OTPStatus.FAILED, e.message ?: "Failed to send OTP")
        }
    }

    fun verifyOTP(req: OTPValidationRequest): OTPResponse {
        return if (otpMap[req.username] == null) {
            OTPResponse(OTPStatus.FAILED, "OTP request not found")
        } else if (System.currentTimeMillis() - otpMap[req.username]!!.second > 300000) {
            otpMap.remove(req.username)
            OTPResponse(OTPStatus.EXPIRED, "OTP request has expired")
        }
        else if (otpMap[req.username]!!.first == req.otp) {
            otpMap.remove(req.username)
            OTPResponse(OTPStatus.SUCCESS, "OTP is valid")
        } else {
            OTPResponse(OTPStatus.INVALID, "OTP is invalid")
        }
    }
}