package com.safarivaya.vayabackend.modules.auth.service

import com.safarivaya.vayabackend.modules.auth.config.JwtProperties
import com.safarivaya.vayabackend.modules.auth.entity.User
import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import org.springframework.stereotype.Service
import java.time.Instant
import java.util.*

@Service
class JwtService(private val props: JwtProperties) {

    private val key = Keys.hmacShaKeyFor(props.secret.toByteArray())

    fun generateAccessToken(user: User): String {
//        println("role=${user.role}, permissions=${RolePermissionRegistry.permissionsFor(user.role)}")
//        val effectiveRules = RolePermissionRegistry.permissionsFor(user.role) + user.extraRules
        val now = Instant.now()
        return Jwts.builder()
            .subject(user.id)
            .claim("email", user.email)
            .claim("role", user.role)
            .claim("tenantId", user.tenantId)
            .claim("permissions", user.effectiveRules.map { it.name })
            .issuedAt(Date.from(now))
            .expiration(Date.from(now.plusSeconds(props.accessTokenTtlMinutes * 60)))
            .signWith(key)
            .compact()
    }

    fun parseAccessToken(token: String): Claims =
        Jwts.parser()
            .verifyWith(key)
            .build()
            .parseSignedClaims(token)
            .payload
}