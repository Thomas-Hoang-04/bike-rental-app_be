package com.cnpm.bikerentalapp.user.controller

import com.cnpm.bikerentalapp.config.exception.model.InvalidUpdate
import com.cnpm.bikerentalapp.config.httpresponse.CRUDResponse
import com.cnpm.bikerentalapp.config.jwt.JWTManager
import com.cnpm.bikerentalapp.user.model.dto.UserDTO
import com.cnpm.bikerentalapp.user.model.httprequest.LoginRequest
import com.cnpm.bikerentalapp.user.model.httprequest.ResetPwdRequest
import com.cnpm.bikerentalapp.user.model.httprequest.UserCreateRequest
import com.cnpm.bikerentalapp.user.model.httpresponse.LoginResponse
import com.cnpm.bikerentalapp.user.principal.UserPrincipal
import com.cnpm.bikerentalapp.user.services.UserServices
import org.springframework.http.ResponseEntity
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/auth")
class AuthController(
    private val userServices: UserServices,
    private val jwtManager: JWTManager,
    private val authManager: AuthenticationManager
) {

    @PostMapping("/login")
    fun login(@Validated @RequestBody req: LoginRequest): ResponseEntity<LoginResponse> {
        val auth = authManager.authenticate(req.mapToAuthToken())
        val principal: UserPrincipal = auth.principal as UserPrincipal

        val token = jwtManager.issue(principal.id, principal.username,
            principal.authorities.map { it.authority })
        val user: UserDTO = userServices.getUserByID(principal.id).mapUserToDTO()

        return ResponseEntity.ok()
            .header("Title", "Login successful")
            .body(LoginResponse(token, user))
    }

    @PostMapping("/signup")
    fun addUsers(@Validated @RequestBody req: UserCreateRequest): ResponseEntity<CRUDResponse<Boolean>> {
        userServices.addUser(req)
        return ResponseEntity.ok()
            .header("Title", "User Registration")
            .body(CRUDResponse("signup", "success", target = true))
    }

    @PatchMapping("/forgot-password")
    fun forgotPassword(@Validated @RequestBody req: ResetPwdRequest): ResponseEntity<CRUDResponse<Boolean>> {
        userServices.updatePassword(req.username, req.newPassword)
        return ResponseEntity.ok()
            .header("Title", "Forgot Password")
            .body(CRUDResponse("forgot-password", "success", target = true))
    }

    @PatchMapping("/reset-password")
    fun resetPassword(@Validated @RequestBody req: ResetPwdRequest): ResponseEntity<CRUDResponse<Boolean>> {
        if (req.oldPassword == null) throw InvalidUpdate("Old password is required")
        if (req.oldPassword == req.newPassword) throw InvalidUpdate("New password must be different from the old one")
        authManager.authenticate(LoginRequest(req.username, req.oldPassword).mapToAuthToken())
        userServices.updatePassword(req.username, req.newPassword)
        return ResponseEntity.ok()
            .header("Title", "Reset Password")
            .body(CRUDResponse("reset-password", "success", target = true))
    }
}