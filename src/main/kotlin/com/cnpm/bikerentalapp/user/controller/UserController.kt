package com.cnpm.bikerentalapp.user.controller

import com.cnpm.bikerentalapp.config.exception.model.InvalidQuery
import com.cnpm.bikerentalapp.config.exception.model.InvalidUpdate
import com.cnpm.bikerentalapp.config.httpresponse.CRUDResponse
import com.cnpm.bikerentalapp.config.httpresponse.QueryResponse
import com.cnpm.bikerentalapp.config.jwt.JWTManager
import com.cnpm.bikerentalapp.user.model.dto.TransactionsDetailsDTO
import com.cnpm.bikerentalapp.user.model.dto.TripDetailsDTO
import com.cnpm.bikerentalapp.user.model.dto.UserDTO
import com.cnpm.bikerentalapp.user.model.httprequest.TopUpRequest
import com.cnpm.bikerentalapp.user.model.httprequest.TransactionRequest
import com.cnpm.bikerentalapp.user.model.httprequest.TripRequest
import com.cnpm.bikerentalapp.user.model.types.TransactionPurpose
import com.cnpm.bikerentalapp.user.model.types.TransactionStatus
import com.cnpm.bikerentalapp.user.services.UserServices
import jakarta.servlet.http.HttpServletRequest
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/user")
class UserController(
    private val userServices: UserServices,
    private val jwtManager: JWTManager,
) {
    private fun headerVerification(authHeader: String, ref: String) {
        if (!authHeader.startsWith("Bearer ")) {
            throw InvalidQuery("Invalid token")
        }
        val token = jwtManager.decode(authHeader.substring(7))
        val issuer = token.getClaim("username").asString() ?: ""
        if (issuer.isBlank() || issuer != ref) {
            throw InvalidQuery("Invalid issuer")
        }
    }

    private fun handleTransaction(action: String, status: TransactionStatus): ResponseEntity<CRUDResponse<TransactionStatus>> =
        when (status) {
            TransactionStatus.SUCCESS -> {
                ResponseEntity.ok()
                    .header("Title", "Transaction")
                    .body(CRUDResponse(action, "success", target = status))
            }
            TransactionStatus.FAILED -> {
                throw InvalidUpdate("Transaction failed due to insufficient balance")
            }
            else -> {
                ResponseEntity.status(202)
                    .header("Title", "Transaction")
                    .body(CRUDResponse(action, "pending", target = status))
            }
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
        val username = token.getClaim("username").asString() ?: ""
        val user: UserDTO = userServices.getUserByUsername(username).mapUserToDTO()
        return ResponseEntity.ok()
            .header("Title", "User")
            .body(QueryResponse("username", 1, mapOf("username" to username), listOf(user)))
    }

    @GetMapping("/transaction/{username}")
    fun getTransactionHistory(
        @RequestHeader(value = "Authorization", required = true) authHeader: String,
        @PathVariable username: String
    ): ResponseEntity<QueryResponse<String, TransactionsDetailsDTO>> {
        headerVerification(authHeader, username)
        val transactions = userServices.getTransactionHistory(username)
        return ResponseEntity.ok()
            .header("Title", "Transaction History")
            .body(QueryResponse("username", transactions.size, mapOf("username" to username), transactions))
    }

    @PostMapping("/transaction")
    fun addTransaction(
        @RequestHeader(value = "Authorization", required = true) authHeader: String,
        @RequestBody req: TransactionRequest
    ): ResponseEntity<CRUDResponse<TransactionStatus>> {
        when (req.purpose) {
            TransactionPurpose.TRIP -> {
                if (req.amount > 0) throw InvalidUpdate("Phí giao dịch không hợp lệ")
            }
            else -> {
                if (req.amount <= 0) throw InvalidUpdate("Số tiền giao dịch phải lớn hơn 0")
            }
        }
        headerVerification(authHeader, req.username)
        return handleTransaction("transaction", userServices.addTransaction(req))
    }

    @GetMapping("/trip/{username}")
    fun getTripDetails(
        @RequestHeader(value = "Authorization", required = true) authHeader: String,
        @PathVariable username: String
    ): ResponseEntity<QueryResponse<String, TripDetailsDTO>> {
        headerVerification(authHeader, username)
        val trips = userServices.getTripDetails(username)
        return ResponseEntity.ok()
            .header("Title", "Trip History")
            .body(QueryResponse("username", trips.size, mapOf("username" to username), trips))
    }

    @PostMapping("/trip")
    fun addTripDetails(
        @RequestHeader(value = "Authorization", required = true) authHeader: String,
        @RequestBody req: TripRequest
    ): ResponseEntity<CRUDResponse<TransactionStatus>> {
        if (req.fee <= 0) throw InvalidUpdate("Phí giao dịch không hợp lệ")
        headerVerification(authHeader, req.username)
        return handleTransaction("Update trip", userServices.addTrip(req))
    }

    @PostMapping("/top-up", "/sharing")
    fun topUpBalance(
            @RequestHeader(value = "Authorization", required = true) authHeader: String,
            @RequestBody req: TopUpRequest,
            orgReq: HttpServletRequest
    ): ResponseEntity<CRUDResponse<TransactionStatus>> {
        val action = orgReq.requestURI.split("/").last()
        if (req.amount <= 0) throw InvalidUpdate("Số tiền giao dịch phải lớn hơn 0")
        if (action == "sharing" && req.from == req.to) throw InvalidUpdate("Không thể chia sẻ điểm cho chính mình")
        if (action == "top-up" && req.from != req.to) throw InvalidUpdate("Không thể nạp tiền cho người khác")
        headerVerification(authHeader, req.from)
        return handleTransaction(action, userServices.topUpBalance(req))
    }
}