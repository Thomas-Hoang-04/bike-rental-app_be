package com.cnpm.bikerentalapp.user.utility

import com.cnpm.bikerentalapp.config.exception.model.InvalidUpdate
import com.cnpm.bikerentalapp.user.model.entity.UserCredential
import com.cnpm.bikerentalapp.user.model.entity.UserDetails
import com.cnpm.bikerentalapp.user.model.httprequest.UserCreateRequest
import com.cnpm.bikerentalapp.user.model.types.UserRole
import com.cnpm.bikerentalapp.user.repository.UserCredentialRepository
import org.springframework.stereotype.Component
import org.springframework.util.ReflectionUtils
import java.lang.reflect.Field
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException
import java.util.*

@Component
class UserUtility(private val userRepo: UserCredentialRepository) {

    private val datePatterns: List<String> = listOf(
        "yyyy-MM-dd",
        "dd-MM-yyyy",
        "yyyy/MM/dd",
        "dd/MM/yyyy",
        "yyyy.MM.dd",
        "dd.MM.yyyy",
    )

    fun verifyUserExistence(username: String): Boolean = userRepo.getByUsername(username).isPresent

    fun verifyDateFormat(date: String): Optional<LocalDate> {
        for (pattern in datePatterns) {
            try {
                val formatter: DateTimeFormatter =  DateTimeFormatter.ofPattern(pattern)
                val newDate: LocalDate = formatter.parse(date, LocalDate::from)
                return Optional.of(newDate)
            } catch (e: DateTimeParseException) {
                continue
            }
        }
        return Optional.empty()
    }

    fun checkPhoneNumber(phoneNum: String): Boolean {
        val phonePattern = Regex("^0[1-9][0-9]{8}$")
        return phonePattern.matches(phoneNum)
    }

    fun checkEmail(email: String): Boolean {
        val emailPattern = Regex("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$")
        return emailPattern.matches(email)
    }

    fun mapCreateUserToEntity(req: UserCreateRequest, pwd: String): UserCredential {
        val newUser = UserCredential(
            username = req.username,
            password = pwd,
            role = req.role ?: UserRole.USER
        )
        val date: LocalDate = this.verifyDateFormat(req.details.dob).orElseThrow {
            InvalidUpdate("Date of birth is invalid")
        }
        val details = UserDetails(
            name = req.details.name,
            email = req.details.email,
            phoneNum = req.details.phoneNum,
            dob = date,
            credential = newUser
        )
        val field: Field = UserCredential::class.java.getDeclaredField("details")
        field.isAccessible = true
        ReflectionUtils.setField(field, newUser, details)
        return newUser
    }
}