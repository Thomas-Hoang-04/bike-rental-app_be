package com.cnpm.bikerentalapp.user.services

import com.cnpm.bikerentalapp.config.exception.model.DataNotFoundException
import com.cnpm.bikerentalapp.config.exception.model.InvalidUpdate
import com.cnpm.bikerentalapp.config.exception.model.UserExisted
import com.cnpm.bikerentalapp.user.model.dto.UserDTO
import com.cnpm.bikerentalapp.user.model.entity.UserCredential
import com.cnpm.bikerentalapp.user.model.httprequest.UserCreateRequest
import com.cnpm.bikerentalapp.user.repository.UserCredentialRepository
import com.cnpm.bikerentalapp.user.utility.UserUtility
import org.springframework.security.crypto.password.Pbkdf2PasswordEncoder
import org.springframework.stereotype.Service

@Service
class UserServices(
    private val util: UserUtility,
    private val userRepo: UserCredentialRepository,
    private val pwd: Pbkdf2PasswordEncoder
) {

    fun getAllUsers(): List<UserDTO> = userRepo.findAll().map { it.mapUserToDTO() }.toList()

    fun getUserByUsername(username: String): UserCredential {
        return userRepo.getByUsername(username).orElseThrow {
            DataNotFoundException("Username $username not found")
        }
    }

    fun addUser(req: UserCreateRequest): UserDTO {
        if (util.verifyUserExistence(req.username)) throw UserExisted("User with username ${req.username} already exists")
        if (!util.checkPhoneNumber(req.details.phoneNum)) throw InvalidUpdate("Phone number is invalid")
        if (!util.checkEmail(req.details.email)) throw InvalidUpdate("Email is invalid")
        val newUser = util.mapCreateUserToEntity(req, pwd.encode(req.password))
        userRepo.save(newUser)
        return newUser.mapUserToDTO()
    }
}