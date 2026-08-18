package com.safarivaya.vayabackend.modules.auth.config

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "app.jwt")
data class JwtProperties(
    val secret: String,
    val accessTokenTtlMinutes: Long = 15,
    val refreshTokenTtlDays: Long = 30
)