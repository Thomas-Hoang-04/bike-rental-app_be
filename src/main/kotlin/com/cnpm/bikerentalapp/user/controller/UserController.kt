package com.cnpm.bikerentalapp.user.controller

import com.cnpm.bikerentalapp.config.httpresponse.QueryResponse
import com.cnpm.bikerentalapp.user.model.dto.UserDTO
import com.cnpm.bikerentalapp.user.services.UserServices
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/user")
class UserController(
    private val userServices: UserServices,
) {
    @GetMapping("/")
    fun getAllUsers() : ResponseEntity<QueryResponse<Unit, UserDTO>> {
        val users: List<UserDTO> = userServices.getAllUsers()
        return ResponseEntity.ok()
            .header("Title", "UserList")
            .body(QueryResponse("all", users.size, mapOf(), users))
    }

    @GetMapping("/{username}")
    fun getUserByUsername(@PathVariable("username") username: String): ResponseEntity<QueryResponse<String, UserDTO>> {
        val user: UserDTO = userServices.getUserByUsername(username).mapUserToDTO()
        return ResponseEntity.ok()
            .header("Title", "User")
            .body(QueryResponse("get", 1, mapOf("username" to username), listOf(user)))
    }
}