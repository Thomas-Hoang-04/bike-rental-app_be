package com.cnpm.bikerentalapp.otp.services

import com.cnpm.bikerentalapp.config.exception.model.DataNotFoundException
import com.cnpm.bikerentalapp.otp.config.TwilioConfig
import com.cnpm.bikerentalapp.otp.model.OTPRequest
import com.cnpm.bikerentalapp.otp.model.OTPResponse
import com.cnpm.bikerentalapp.otp.model.OTPStatus
import com.cnpm.bikerentalapp.otp.model.OTPValidationRequest
import com.cnpm.bikerentalapp.user.services.UserServices
import com.twilio.rest.api.v2010.account.Message

import com.twilio.type.PhoneNumber
import org.springframework.stereotype.Service
import java.text.DecimalFormat
import kotlin.random.Random

@Service
class OTPService(
    private val twilioConfig: TwilioConfig,
    private val userServices: UserServices
) {
    private val otpMap: HashMap<String, Pair<String, Long>> = HashMap()

    private fun generateOTP(): String = DecimalFormat("00000").format(Random.nextInt(100000))

    private fun generateMessage(otp: String): String = "Mã xác thực của bạn là: $otp. Mã xác thực này sẽ hết hạn sau 5 phút."

    fun sendOTP(req: OTPRequest): OTPResponse {
        try {
            userServices.getUserByUsername(req.username)
            if (otpMap[req.username] != null && System.currentTimeMillis() - otpMap[req.username]!!.second < 60000) {
                return OTPResponse(OTPStatus.INVALID, "Hãy chờ ít nhất 60 giây trước khi yêu cầu lại mã xác thực")
            }
            val tar = PhoneNumber(req.phoneNumber)
            val from = PhoneNumber(twilioConfig.phoneNumber)
            val otp: String = generateOTP()
            val message: String = generateMessage(otp)
            Message.creator(tar, from, message).create()
            otpMap[req.username] = Pair(otp, System.currentTimeMillis())
            return OTPResponse(OTPStatus.SUCCESS, "Gửi mã OTP thành công")
        } catch (e: DataNotFoundException) {
            return OTPResponse(OTPStatus.INVALID, e.message)
        } catch (e: Exception) {
            return OTPResponse(OTPStatus.FAILED, e.message ?: "Gửi mã OTP thất bại")
        }
    }

    fun verifyOTP(req: OTPValidationRequest): OTPResponse {
        return if (otpMap[req.username] == null) {
            OTPResponse(OTPStatus.FAILED, "Không tìm thấy mã OTP")
        } else if (System.currentTimeMillis() - otpMap[req.username]!!.second > 300000) {
            otpMap.remove(req.username)
            OTPResponse(OTPStatus.EXPIRED, "Mã OTP đã hết hạn. Vui lòng yêu cầu mã mới")
        }
        else if (otpMap[req.username]!!.first == req.otp) {
            otpMap.remove(req.username)
            OTPResponse(OTPStatus.SUCCESS, "Mã OTP hợp lệ")
        } else {
            OTPResponse(OTPStatus.INVALID, "Mã OTP không hợp lệ. Vui lòng thử lại")
        }
    }
}