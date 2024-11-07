package com.cnpm.bikerentalapp.user.controller

import com.cnpm.bikerentalapp.config.jwt.JWTManager
import com.cnpm.bikerentalapp.user.model.httprequest.LoginRequest
import com.cnpm.bikerentalapp.user.model.httpresponse.LoginResponse
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

@RestController
@RequestMapping("/api/auth")
class UserController {

    @Autowired
    private lateinit var jwtManager: JWTManager

    @GetMapping("/test")
    fun test(): String {
        return "If you see, part 2 completed"
    }

    @PostMapping("/login")
    fun login(@Validated @RequestBody req: LoginRequest): LoginResponse {
        val token = jwtManager.issue(
            UUID.fromString("0ff2c460-06c7-4aca-ad4e-cdb3ef7f6a88"),
            req.username,
            listOf("USER"))
        return LoginResponse(token)
    }


}