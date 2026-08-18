package com.safarivaya.vayabackend.common.exception

import jakarta.servlet.http.HttpServletRequest
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class GlobalExceptionHandler {

    private val log = LoggerFactory.getLogger(javaClass)

    @ExceptionHandler(ApplicationException::class)
    fun handleApplicationException(ex: ApplicationException, request: HttpServletRequest): ResponseEntity<ErrorResponse> {
        if (ex.status.is5xxServerError) {
            log.error("Application Error - processing request: ${request.method} ${request.requestURI}", ex)
        } else log.warn(ex.message)
        return ResponseEntity.status(ex.status)
            .body(
                ErrorResponse(
                    ex.status.value(),
                    ex.errorCode,
                    ex.message ?: "Internal Server Error",
                    request.requestURI
                )
            )
    }

    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleValidation(ex: MethodArgumentNotValidException, request: HttpServletRequest): ResponseEntity<ErrorResponse> {
        val fieldErrors = ex.bindingResult.fieldErrors.map {
            FieldError(it.field, it.defaultMessage ?: "Invalid value")
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
            ErrorResponse(400, "VALIDATION_ERROR", "Request validation failed", request.requestURI, fieldErrors = fieldErrors)
        )
    }

    @ExceptionHandler(Exception::class)
    fun handleUnexpected(ex: Exception, request: HttpServletRequest): ResponseEntity<ErrorResponse> {
        log.error("Unhandled exception:", ex)
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
            ErrorResponse(500, "INTERNAL_SERVER_ERROR", "An unexpected error occurred", request.requestURI)
        )
    }
}