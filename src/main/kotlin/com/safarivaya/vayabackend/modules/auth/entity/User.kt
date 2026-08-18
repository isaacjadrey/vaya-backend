package com.safarivaya.vayabackend.modules.auth.entity

import jakarta.persistence.CollectionTable
import jakarta.persistence.Column
import jakarta.persistence.ElementCollection
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.FetchType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.Table
import java.time.LocalDateTime

enum class UserRole {
    SUPER_ADMIN, ADMIN, SUPERVISOR, AUDITOR, OPERATOR, CLERK, INSPECTOR, DRIVER, NONE,
}
enum class UserStatus { INACTIVE, ACTIVE, SUSPENDED }

@Entity
@Table(name = "users")
class User(
    @Id val id: String,
    var tenantId: String? = null,
    @Column(nullable = false) var name: String,
    @Column(nullable = false) var email: String,
    var phoneNumber: String? = null,
    @Column(nullable = false) var passwordHash: String,
    @Enumerated(EnumType.STRING) var role: UserRole = UserRole.NONE,
    @Enumerated(EnumType.STRING) var status: UserStatus = UserStatus.INACTIVE,
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "user_permissions", joinColumns = [JoinColumn(name = "user_id")])
    @Enumerated(EnumType.STRING)
    @Column(name = "permission")
    var extraRules: MutableSet<Rules> = mutableSetOf(),
    var verificationCode: String? = null,
    var verificationExpiresAt: LocalDateTime? = null,
    @Column(nullable = false) var createdAt: LocalDateTime = LocalDateTime.now()
)