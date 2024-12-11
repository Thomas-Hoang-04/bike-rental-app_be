package com.cnpm.bikerentalapp.config.exception.handler

import com.cnpm.bikerentalapp.config.exception.model.ErrorResponse
import com.fasterxml.jackson.databind.ObjectMapper
import jakarta.servlet.http.HttpServletResponse
import java.time.LocalDateTime

object ErrorResponseWriter {
   fun write(res: HttpServletResponse, e: Exception, uri: String) {
        val errorRes = ErrorResponse(
            LocalDateTime.now().toString(),
            HttpServletResponse.SC_UNAUTHORIZED,
            "Unauthorized",
            e.javaClass,
            e.message ?: "Invalid JWT due to ${e.message}",
            uri
        )
        res.status = HttpServletResponse.SC_UNAUTHORIZED
        res.contentType = "application/json"
        res.characterEncoding = "UTF-8"
        res.writer.write(
            ObjectMapper().writeValueAsString(errorRes)
        )
    }
}