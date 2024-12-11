package com.cnpm.bikerentalapp.user.utility

import com.cnpm.bikerentalapp.config.exception.model.InvalidUpdate
import com.cnpm.bikerentalapp.config.utility.Geolocation
import com.cnpm.bikerentalapp.user.model.entity.UserCredential
import com.cnpm.bikerentalapp.user.model.entity.UserDetails
import com.cnpm.bikerentalapp.user.model.httprequest.UserCreateRequest
import com.cnpm.bikerentalapp.user.model.types.UserRole
import com.cnpm.bikerentalapp.user.repository.UserRepository
import org.springframework.stereotype.Component
import org.springframework.util.ReflectionUtils
import java.lang.reflect.Field
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException
import java.util.*
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt

@Component
class UserUtility(private val userRepo: UserRepository) {

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

    private fun calculateHaversineDistance(lat1: Double, lng1: Double, lat2: Double, lng2: Double): Double {
        // TODO: Implement parallel haversine distance calculation in Reactive
        val earthRadius = 6371.0
        val dLat = Math.toRadians(lat2 - lat1)
        val dLng = Math.toRadians(lng2 - lng1)
        val a = sin(dLat / 2) * sin(dLat / 2) +
                cos(Math.toRadians(lat1)) * cos(Math.toRadians(lat2)) *
                sin(dLng / 2) * sin(dLng / 2)
        val c = 2 * atan2(sqrt(a), sqrt(1 - a))
        return earthRadius * c
    }

    fun calculateDistance(route: List<Geolocation>): Double {
        var distance = 0.0
        for (i in 0 until route.size - 1) {
            val (lat1, lng1) = route[i]
            val (lat2, lng2) = route[i + 1]
            distance += calculateHaversineDistance(lat1, lng1, lat2, lng2)
        }
        return distance
    }
}