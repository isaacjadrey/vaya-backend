package com.safarivaya.vayabackend.modules.auth.repository

import com.safarivaya.vayabackend.modules.auth.entity.User
import org.springframework.data.jpa.repository.JpaRepository

interface UserRepository : JpaRepository<User, String> {
    fun findByEmail(email: String): User?
    fun existsByEmail(email: String): Boolean
}