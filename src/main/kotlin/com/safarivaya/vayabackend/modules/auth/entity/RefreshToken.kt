package com.safarivaya.vayabackend.modules.auth.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.time.LocalDateTime

@Entity
@Table(name = "refresh_tokens")
class RefreshToken(
    @Id val id: String,
    @Column(nullable = false) val userId: String,
    @Column(nullable = false, unique = true) val tokenHash: String,
    @Column(nullable = false) val expiresAt: LocalDateTime,
    var revoked: Boolean = false,
    @Column(nullable = false) val createdAt: LocalDateTime = LocalDateTime.now()
)