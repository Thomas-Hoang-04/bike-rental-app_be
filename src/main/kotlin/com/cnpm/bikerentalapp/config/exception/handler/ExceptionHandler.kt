package com.cnpm.bikerentalapp.config.exception.handler

import com.cnpm.bikerentalapp.config.exception.model.*
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.context.request.WebRequest
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler
import java.time.LocalDateTime

@ControllerAdvice
class ExceptionHandler: ResponseEntityExceptionHandler() {

    private fun generateTemplate(ex: Exception, req: WebRequest, status: HttpStatus): ErrorResponse {
        return ErrorResponse(
            timestamp = LocalDateTime.now().toString(),
            status = status.value(),
            error = status.name,
            type = ex.javaClass,
            message = ex.message ?: status.reasonPhrase,
            path = req.getDescription(false) // Get the path of the request
        )
    }

    @ExceptionHandler(DataNotFoundException::class)
    fun handleBikeNotFoundException(ex: RuntimeException, req: WebRequest): ResponseEntity<ErrorResponse> {
        return ResponseEntity(generateTemplate(ex, req, HttpStatus.NOT_FOUND), HttpStatus.NOT_FOUND)
    }

    @ExceptionHandler(InvalidQuery::class, InvalidUpdate::class)
    fun handleInvalidQuery(ex: IllegalArgumentException, req: WebRequest): ResponseEntity<ErrorResponse> {
        return ResponseEntity(generateTemplate(ex, req, HttpStatus.BAD_REQUEST), HttpStatus.BAD_REQUEST)
    }

    @ExceptionHandler(UserExisted::class)
    fun handleUserExisted(ex: UserExisted, req: WebRequest): ResponseEntity<ErrorResponse> {
        return ResponseEntity(generateTemplate(ex, req, HttpStatus.CONFLICT), HttpStatus.CONFLICT)
    }

    @ExceptionHandler(BadCredentialsException::class)
    fun handleBadCredentials(ex: BadCredentialsException, req: WebRequest): ResponseEntity<ErrorResponse> {
        return ResponseEntity(generateTemplate(ex, req, HttpStatus.UNAUTHORIZED), HttpStatus.UNAUTHORIZED)
    }

    @ExceptionHandler(Exception::class)
    fun handleAllExceptions(ex: Exception, req: WebRequest): ResponseEntity<ErrorResponse> {
        return ResponseEntity(generateTemplate(ex, req, HttpStatus.INTERNAL_SERVER_ERROR), HttpStatus.INTERNAL_SERVER_ERROR)
    }
}