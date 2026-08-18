package com.safarivaya.vayabackend.common.exception

import org.springframework.http.HttpStatus

sealed class ApplicationException(
    message: String,
    val status: HttpStatus,
    val errorCode: String
) : RuntimeException(message) {

    class NotFoundException(message: String, errorCode: String = "NOT_FOUND") :
            ApplicationException(message, HttpStatus.NOT_FOUND, errorCode)

    class ConflictException(message: String, errorCode: String = "CONFLICT") :
            ApplicationException(message, HttpStatus.CONFLICT, errorCode)

    class ValidationException(message: String, errorCode: String = "VALIDATION_ERROR") :
            ApplicationException(message, HttpStatus.BAD_REQUEST, errorCode)

    class UnauthorizedException(message: String, errorCode: String = "UNAUTHORIZED") :
            ApplicationException(message, HttpStatus.UNAUTHORIZED, errorCode)

    class ForbiddenException(message: String, errorCode: String = "FORBIDDEN") :
            ApplicationException(message, HttpStatus.FORBIDDEN, errorCode)
}