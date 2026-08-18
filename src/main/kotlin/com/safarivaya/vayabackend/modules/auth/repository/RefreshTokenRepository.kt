package com.safarivaya.vayabackend.modules.auth.repository

import com.safarivaya.vayabackend.modules.auth.entity.RefreshToken
import org.springframework.data.jpa.repository.JpaRepository

interface RefreshTokenRepository : JpaRepository<RefreshToken, String> {
    fun findByTokenHash(tokenHash: String): RefreshToken?
    fun deleteAllByUserId(userId: String)
}