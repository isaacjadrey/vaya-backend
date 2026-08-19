package com.safarivaya.vayabackend.modules.auth.entity

import com.safarivaya.vayabackend.modules.auth.service.RolePermissionRegistry
import jakarta.persistence.*
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
) {
    @get:Transient
    val effectiveRules: Set<Rules>
        get() = RolePermissionRegistry.permissionsFor(role) + extraRules
}

//fun User.effectivePermissions(): Set<Rules> = RolePermissionRegistry.permissionsFor(role) + extraRules
