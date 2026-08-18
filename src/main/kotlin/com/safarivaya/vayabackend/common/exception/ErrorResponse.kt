package com.safarivaya.vayabackend.common.exception

import java.time.Instant

data class ErrorResponse(
    val status: Int,
    val errorCode: String,
    val message: String,
    val path: String,
    val timestamp: Instant = Instant.now(),
    val fieldErrors: List<FieldError> = emptyList()
)

data class FieldError(val field: String, val message: String)
