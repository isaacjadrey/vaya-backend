package com.safarivaya.vayabackend.modules.auth.dto

import com.safarivaya.vayabackend.modules.auth.entity.Rules
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Pattern

data class RegisterRequest(
    @field:NotBlank(message = "Full name is required")
    val name: String,
    @field:Email(message = "Valid email is required")
    val email: String,
    val phoneNumber: String? = null,
    @field:Pattern(
        regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!]).*$",
        message = "Password must be at least 9 characters long and contain at least one digit, " +
                "uppercase and lowercase characters."
    )
   val password: String
)

data class VerifyRequest(
    @field:Email val email: String,
    @field:NotBlank val code: String
)

data class LoginRequest(
    @field:NotBlank(message = "Email cannot be blank")
    @field:Email(message = "Valid email is required")
    val email: String,
    @field:NotBlank(message = "Password cannot be blank") val password: String
)

data class RefreshRequest(@field:NotBlank val refreshToken: String)

data class TokenPair(val accessToken: String, val refreshToken: String)

data class UserResponse(
    val id: String,
    val tenantId: String?,
    val name: String,
    val email: String,
    val phoneNumber: String?,
    val role: String,
    val status: String,
    val permissions: Set<Rules>
)

data class AuthResponse(val user: UserResponse, val tokens: TokenPair)