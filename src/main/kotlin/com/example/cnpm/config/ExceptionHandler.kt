package com.example.cnpm.config

import com.example.cnpm.bike.exception.DataNotFoundException
import com.example.cnpm.bike.exception.InvalidUpdate
import com.example.cnpm.bike.exception.InvalidQuery
import com.example.cnpm.bike.model.httpresponse.ErrorResponse
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.context.request.WebRequest
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler
import java.time.LocalDateTime

@ControllerAdvice
class ExceptionHandler: ResponseEntityExceptionHandler() {

    private fun generateTemplate(ex: Exception, req: WebRequest, status: HttpStatus): ErrorResponse {
        return ErrorResponse(
            timestamp = LocalDateTime.now(),
            status = status.value(),
            error = status.name,
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

    @ExceptionHandler(Exception::class)
    fun handleAllExceptions(ex: Exception, req: WebRequest): ResponseEntity<ErrorResponse> {
        return ResponseEntity(generateTemplate(ex, req, HttpStatus.INTERNAL_SERVER_ERROR), HttpStatus.INTERNAL_SERVER_ERROR)
    }
}