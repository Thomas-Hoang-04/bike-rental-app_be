package com.cnpm.bikerentalapp.user.services

import com.cnpm.bikerentalapp.bike.services.BikeServices
import com.cnpm.bikerentalapp.config.exception.model.DataNotFoundException
import com.cnpm.bikerentalapp.config.exception.model.InvalidUpdate
import com.cnpm.bikerentalapp.config.exception.model.UserExisted
import com.cnpm.bikerentalapp.user.model.dto.TicketDetailsDTO
import com.cnpm.bikerentalapp.user.model.dto.TransactionsDetailsDTO
import com.cnpm.bikerentalapp.user.model.dto.TripDetailsDTO
import com.cnpm.bikerentalapp.user.model.dto.UserDTO
import com.cnpm.bikerentalapp.user.model.entity.UserCredential
import com.cnpm.bikerentalapp.user.model.httprequest.*
import com.cnpm.bikerentalapp.user.model.types.TicketTypes
import com.cnpm.bikerentalapp.user.model.types.TransactionPurpose
import com.cnpm.bikerentalapp.user.model.types.TransactionStatus
import com.cnpm.bikerentalapp.user.repository.UserRepository
import com.cnpm.bikerentalapp.user.utility.UserUtility
import org.locationtech.jts.geom.GeometryFactory
import org.springframework.security.crypto.password.Pbkdf2PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.util.ReflectionUtils
import java.lang.reflect.Field
import java.time.OffsetDateTime
import java.time.format.DateTimeParseException
import java.util.*

@Service
class UserServices(
    private val util: UserUtility,
    private val userRepo: UserRepository,
    private val pwd: Pbkdf2PasswordEncoder,
    private val bikeServices: BikeServices
) {

    fun getAllUsers(): List<UserDTO> = userRepo.findAll().map { it.mapUserToDTO() }.toList()

    fun getUserByID(id: UUID): UserCredential = userRepo.findById(id).orElseThrow {
        DataNotFoundException("User with ID $id not found")
    }

    fun getUserByUsername(username: String): UserCredential {
        return userRepo.getByUsername(username).orElseThrow {
            DataNotFoundException("Username $username not found")
        }
    }

    fun getTransactionHistory(username: String): List<TransactionsDetailsDTO> {
        val user = getUserByUsername(username)
        return user.transactions.map { it.mapEntityToDTO() }
    }

    fun getTripDetails(username: String): List<TripDetailsDTO> {
        val user = getUserByUsername(username)
        return user.trips.map {
            val route = userRepo.getTravelRoute(it.trip)
            it.mapEntityToDTO(route)
        }
    }

    fun getTicketDetails(username: String): List<TicketDetailsDTO> {
        val user = getUserByUsername(username)
        return user.tickets.map { it.mapEntityToDTO() }
    }

    fun buyTicket(req: TicketRequest): TransactionStatus {
        val user = getUserByUsername(req.username)
        val transactionList = mutableListOf<TransactionStatus>()
        if (req.price < user.balance) return TransactionStatus.FAILED
        repeat(req.quantity) {
            user.tickets.add(req.mapToEntity(user))
            userRepo.save(user)
            transactionList.add(
                addTransaction(
                    TransactionRequest(
                        req.username,
                        -req.price,
                        TransactionPurpose.TICKET,
                        "Thanh toán vé ${if (req.ticket == TicketTypes.DAILY) "ngày" else "tháng"}"
                    )
                )
            )
        }
        return if (transactionList.any { it == TransactionStatus.FAILED }) TransactionStatus.FAILED
            else TransactionStatus.SUCCESS
    }

    fun checkUserExistence(username: String): Boolean = util.verifyUserExistence(username)

    fun addUser(req: UserCreateRequest): UserDTO {
        if (util.verifyUserExistence(req.username)) throw UserExisted("User ${req.username} already exists")
        if (!util.checkPhoneNumber(req.details.phoneNum)) throw InvalidUpdate("Phone number is invalid")
        if (!util.checkEmail(req.details.email)) throw InvalidUpdate("Email is invalid")
        val newUser = util.mapCreateUserToEntity(req, pwd.encode(req.password))
        userRepo.save(newUser)
        return newUser.mapUserToDTO()
    }

    fun addTransaction(req: TransactionRequest): TransactionStatus {
        val user = getUserByUsername(req.username)
        val status =
            if (req.amount < 0 && user.balance < -req.amount) TransactionStatus.FAILED
            else TransactionStatus.SUCCESS
        val transaction = req.mapToEntity(user, status)
        if (status == TransactionStatus.SUCCESS) user.updateBalance(req.amount)
        user.transactions.add(transaction)
        userRepo.save(user)
        return status
    }

    fun addTrip(req: TripRequest): TransactionStatus {
        try {
            val startTime = OffsetDateTime.parse(req.startTime)
            val endTime = OffsetDateTime.parse(req.endTime)
            if (startTime.isAfter(endTime)) throw InvalidUpdate("Invalid time range")
        } catch(e: DateTimeParseException) {
            throw InvalidUpdate("Invalid time format")
        }
        val user = getUserByUsername(req.username)
        if (req.fee < user.balance) return TransactionStatus.FAILED
        bikeServices.getBikeByPlate(req.bikePlate)
        val postGISCoordinates = req.route.map { it.mapToPostGISCoordinates() }.toTypedArray()
        val route = GeometryFactory().createLineString(postGISCoordinates)
        val distance = util.calculateDistance(req.route)
        val trip = req.mapRequestToEntity(route, distance, user)
        user.trips.add(trip)
        userRepo.save(user)
        return addTransaction(TransactionRequest(
            req.username,
            -req.fee,
            TransactionPurpose.TRIP,
            "Thanh toán chuyến đi"
        ))
    }

    fun updatePassword(username: String, password: String) {
        val user = getUserByUsername(username)
        val field: Field = UserCredential::class.java.getDeclaredField("password")
        field.isAccessible = true
        ReflectionUtils.setField(field, user, pwd.encode(password))
        userRepo.save(user)
    }

    fun topUpBalance(req: TopUpRequest): TransactionStatus {
        val userSrc = getUserByUsername(req.from)
        if (req.from == req.to) {
            userSrc.let {
                it.updateBalance(req.amount)
                it.transactions.add(req.mapToTopUpTransactions(it, TransactionStatus.SUCCESS))
                userRepo.save(it)
            }
        } else {
            val userDest = getUserByUsername(req.to)
            if (userSrc.balance < req.amount) return TransactionStatus.FAILED
            userSrc.let {
                it.updateBalance(-req.amount)
                it.transactions.add(req.mapToSrcTransactions(it, TransactionStatus.SUCCESS))
                userRepo.save(it)
            }
            userDest.let {
                it.updateBalance(req.amount)
                it.transactions.add(req.mapToDestTransactions(it, TransactionStatus.SUCCESS))
                userRepo.save(it)
            }
        }
        return TransactionStatus.SUCCESS
    }
}