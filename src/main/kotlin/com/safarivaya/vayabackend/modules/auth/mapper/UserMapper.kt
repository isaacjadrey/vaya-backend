package com.safarivaya.vayabackend.modules.auth.mapper

import com.safarivaya.vayabackend.modules.auth.dto.UserResponse
import com.safarivaya.vayabackend.modules.auth.entity.User

fun User.toResponse() = UserResponse(
    id = id,
    tenantId = tenantId,
    name = name,
    email = email,
    phoneNumber = phoneNumber,
    role = role.name,
    status = status.name,
    permissions = effectiveRules
)