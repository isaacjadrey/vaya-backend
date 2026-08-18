package com.safarivaya.vayabackend.modules.auth.service

import com.safarivaya.vayabackend.modules.auth.config.JwtProperties
import com.safarivaya.vayabackend.common.exception.ApplicationException
import com.safarivaya.vayabackend.common.id.IdGenerator
import com.safarivaya.vayabackend.modules.auth.dto.*
import com.safarivaya.vayabackend.modules.auth.entity.RefreshToken
import com.safarivaya.vayabackend.modules.auth.entity.User
import com.safarivaya.vayabackend.modules.auth.entity.UserStatus
import com.safarivaya.vayabackend.modules.auth.mapper.toResponse
import com.safarivaya.vayabackend.modules.auth.repository.RefreshTokenRepository
import com.safarivaya.vayabackend.modules.auth.repository.UserRepository
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.security.MessageDigest
import java.security.SecureRandom
import java.time.LocalDateTime
import java.util.*

@Service
class AuthService(
    private val userRepo: UserRepository,
    private val refreshTokenRepo: RefreshTokenRepository,
    private val passwordEncoder: PasswordEncoder,
    private val jwtService: JwtService,
    private val mailService: MailService,
    private val idGenerator: IdGenerator,
    private val jwtProperties: JwtProperties
) {
    @Transactional
    fun register(request: RegisterRequest): UserResponse {
        if (userRepo.existsByEmail(request.email)) {
            throw ApplicationException.ConflictException("An account with this email already exists", " EMAIL_TAKEN")
        }
        val code = generateVerificationCode()
        val user = userRepo.save(
            User(
                id = idGenerator.next(),
                name = request.name,
                email = request.email,
                phoneNumber = request.phoneNumber,
                passwordHash = passwordEncoder.encode(request.password)!!,
                verificationCode = code,
                verificationExpiresAt = LocalDateTime.now().plusMinutes(jwtProperties.accessTokenTtlMinutes),
            )
        )
        mailService.sendVerificationCode(user.email, code)
        return user.toResponse()
    }

    @Transactional
    fun verify(request: VerifyRequest): UserResponse {
        val user = userRepo.findByEmail(request.email)
            ?: throw ApplicationException.NotFoundException("No account found for this email")

        if (user.status == UserStatus.ACTIVE) return user.toResponse()

        if (user.verificationCode != request.code) {
            throw ApplicationException.ValidationException("Invalid verification code", "INVALID_CODE")
        }

        if (LocalDateTime.now() > user.verificationExpiresAt) {
            throw ApplicationException.ValidationException("Verification code has expired", "EXPIRED_CODE")
        }

        user.status = UserStatus.ACTIVE
        user.verificationCode = null
        user.verificationExpiresAt = null
        return userRepo.save(user).toResponse()
    }

    @Transactional
    fun login(request: LoginRequest): AuthResponse {
        val user = userRepo.findByEmail(request.email)
            ?: throw ApplicationException.UnauthorizedException("Invalid email or password")

        if (!passwordEncoder.matches(request.password, user.passwordHash)) {
            throw ApplicationException.UnauthorizedException(
                "Invalid email or password",
                "INVALID_PASSWORD_OR_PASSWORD"
            )
        }
        if (user.status != UserStatus.ACTIVE) {
            throw ApplicationException.UnauthorizedException("Account is not verified", "ACCOUNT_NOT_VERIFIED")
        }

        return AuthResponse(user.toResponse(), issueTokenPair(user))
    }

    @Transactional
    fun refresh(request: RefreshRequest): TokenPair {
        val hash = hashToken(request.refreshToken)
        val stored = refreshTokenRepo.findByTokenHash(hash)
            ?.takeIf { !it.revoked && it.expiresAt.isAfter(LocalDateTime.now()) }
            ?: throw ApplicationException.UnauthorizedException(
                "Invalid or expired refresh token",
                "INVALID_EXPIRED_REFRESH_TOKEN"
            )

        stored.revoked = true
        refreshTokenRepo.save(stored)

        val user = userRepo.findById(stored.userId)
            .orElseThrow { ApplicationException.UnauthorizedException("Account no longer exists") }

        return issueTokenPair(user)
    }

    fun issueTokenPair(user: User): TokenPair {
        val accessToken = jwtService.generateAccessToken(user)
        val rawRefreshToken = generateRefreshTokenValue()

        refreshTokenRepo.save(
            RefreshToken(
                id = idGenerator.next(),
                userId = user.id,
                tokenHash = hashToken(rawRefreshToken),
                expiresAt = LocalDateTime.now().plusDays(jwtProperties.refreshTokenTtlDays)
            )
        )

        return TokenPair(accessToken, rawRefreshToken)
    }

    private fun generateVerificationCode(): String =
        SecureRandom().nextInt(1_000_000).toString().padStart(6, '0')

    private fun generateRefreshTokenValue(): String {
        val bytes = ByteArray(32)
        SecureRandom().nextBytes(bytes)
        return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes)
    }

    private fun hashToken(token: String): String =
        MessageDigest.getInstance("SHA-256").digest(token.toByteArray()).joinToString("") { "%02x".format(it) }
}