package com.cnpm.bikerentalapp.user.controller

import com.cnpm.bikerentalapp.config.JWTIssuer
import com.cnpm.bikerentalapp.user.model.httprequest.LoginRequest
import com.cnpm.bikerentalapp.user.model.httpresponse.LoginResponse
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/user")
class UserController {

    @Autowired
    private lateinit var jwtIssuer: JWTIssuer

    @GetMapping("/test")
    fun test(): String {
        return "Hello World"
    }

    @PostMapping("/auth/login")
    fun login(@Validated @RequestBody req: LoginRequest): LoginResponse {
        val token = jwtIssuer.issue(1, req.username, listOf("USER"))
        return LoginResponse(token)
    }
}