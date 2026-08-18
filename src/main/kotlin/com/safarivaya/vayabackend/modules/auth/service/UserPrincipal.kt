package com.safarivaya.vayabackend.modules.auth.service

data class UserPrincipal(
    val userId: String,
    val email: String,
    val role: String,
    val tenantId: String?,
    val permissions: Set<String>
)
