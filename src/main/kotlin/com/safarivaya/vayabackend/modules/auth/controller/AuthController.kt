package com.safarivaya.vayabackend.modules.auth.controller

import com.safarivaya.vayabackend.modules.auth.dto.AuthResponse
import com.safarivaya.vayabackend.modules.auth.dto.LoginRequest
import com.safarivaya.vayabackend.modules.auth.dto.RefreshRequest
import com.safarivaya.vayabackend.modules.auth.dto.RegisterRequest
import com.safarivaya.vayabackend.modules.auth.dto.TokenPair
import com.safarivaya.vayabackend.modules.auth.dto.UserResponse
import com.safarivaya.vayabackend.modules.auth.dto.VerifyRequest
import com.safarivaya.vayabackend.modules.auth.service.AuthService
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/auth")
class AuthController(private val authService: AuthService) {

    @PostMapping("/register")
    fun register(@Valid @RequestBody request: RegisterRequest): ResponseEntity<UserResponse> =
        ResponseEntity.status(HttpStatus.CREATED).body(authService.register(request))

    @PostMapping("/verify")
    fun verify(@Valid @RequestBody request: VerifyRequest): ResponseEntity<UserResponse> =
        ResponseEntity.ok(authService.verify(request))

    @PostMapping("/login")
    fun login(@Valid @RequestBody request: LoginRequest): ResponseEntity<AuthResponse> =
        ResponseEntity.ok(authService.login(request))

    @PostMapping("/refresh")
    fun refresh(@Valid @RequestBody request: RefreshRequest): ResponseEntity<TokenPair> =
        ResponseEntity.ok(authService.refresh(request))
}