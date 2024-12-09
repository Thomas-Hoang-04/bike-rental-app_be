package com.cnpm.bikerentalapp.user.controller

import com.cnpm.bikerentalapp.config.exception.model.InvalidQuery
import com.cnpm.bikerentalapp.config.exception.model.InvalidUpdate
import com.cnpm.bikerentalapp.config.httpresponse.CRUDResponse
import com.cnpm.bikerentalapp.config.httpresponse.QueryResponse
import com.cnpm.bikerentalapp.config.jwt.JWTManager
import com.cnpm.bikerentalapp.user.model.dto.TopUpRequest
import com.cnpm.bikerentalapp.user.model.dto.UserDTO
import com.cnpm.bikerentalapp.user.model.httprequest.LoginRequest
import com.cnpm.bikerentalapp.user.model.httprequest.ResetPwdRequest
import com.cnpm.bikerentalapp.user.services.UserServices
import org.springframework.http.ResponseEntity
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/user")
class UserController(
    private val userServices: UserServices,
    private val jwtManager: JWTManager,
    private val authManager: AuthenticationManager
) {
    private fun headerVerification(authHeader: String): String {
        if (!authHeader.startsWith("Bearer ")) {
            throw InvalidQuery("Invalid token")
        }
        val token = jwtManager.decode(authHeader.substring(7))
        val issuer = token.getClaim("username").asString() ?: ""
        return issuer
    }

    @GetMapping("/all")
    fun getAllUsers() : ResponseEntity<QueryResponse<Unit, UserDTO>> {
        val users: List<UserDTO> = userServices.getAllUsers()
        return ResponseEntity.ok()
            .header("Title", "UserList")
            .body(QueryResponse("all", users.size, mapOf(), users))
    }

    @GetMapping("/me")
    fun getUserByUsername(@RequestHeader(value = "Authorization", required = true) authHeader: String): ResponseEntity<QueryResponse<String, UserDTO>> {
        if (!authHeader.startsWith("Bearer ")) {
            throw InvalidQuery("Invalid token")
        }
        val token = jwtManager.decode(authHeader.substring(7))
        val username = token.getClaim("from").asString() ?: ""
        val user: UserDTO = userServices.getUserByUsername(username).mapUserToDTO()
        return ResponseEntity.ok()
            .header("Title", "User")
            .body(QueryResponse("get", 1, mapOf("from" to username), listOf(user)))
    }

    @PostMapping("/top-up", "/sharing")
    fun topUpBalance(
            @RequestHeader(value = "Authorization", required = true) authHeader: String,
            @RequestBody req: TopUpRequest
    ): ResponseEntity<CRUDResponse<Boolean>> {
        val issuer = headerVerification(authHeader)
        if (issuer.isBlank() || issuer != req.from) {
            throw InvalidQuery("Invalid issuer")
        }
        userServices.topUpBalance(req)
        return ResponseEntity.ok()
            .header("Title", "Top-up")
            .body(CRUDResponse("top-up", "success", target = true))
    }

    @PatchMapping("/reset-password")
    fun resetPassword(@Validated @RequestBody req: ResetPwdRequest): ResponseEntity<CRUDResponse<Boolean>> {
        if (req.oldPassword == null) throw InvalidUpdate("Old password is required")
        authManager.authenticate(LoginRequest(req.username, req.oldPassword).mapToAuthToken())
        userServices.updatePassword(req.username, req.newPassword)
        return ResponseEntity.ok()
            .header("Title", "Reset Password")
            .body(CRUDResponse("reset-password", "success", target = true))
    }
}