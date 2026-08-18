package com.safarivaya.vayabackend.modules.auth.service

import com.safarivaya.vayabackend.modules.auth.entity.Rules
import com.safarivaya.vayabackend.modules.auth.entity.Rules.*
import com.safarivaya.vayabackend.modules.auth.entity.UserRole

object RolePermissionRegistry {

    fun permissionsFor(role: UserRole): Set<Rules> =
        when (role) {
            UserRole.SUPER_ADMIN -> superAdminRules
            UserRole.ADMIN -> adminRules
            UserRole.SUPERVISOR -> supervisorRules
            UserRole.AUDITOR -> auditorRules
            UserRole.OPERATOR -> operatorRules
            UserRole.CLERK -> clerkRules
            UserRole.INSPECTOR -> inspectorRules
            UserRole.DRIVER -> driverRules
            UserRole.NONE -> emptySet()
        }
}

private val excludedSuperAdminRules = listOf(ISSUE_TICKET, VERIFY_TICKET, REFUND_TICKET)
private val superAdminRules = Rules.entries.toSet() - excludedSuperAdminRules.toSet()

private val excludedAdminRules = listOf(ISSUE_TICKET, VERIFY_TICKET, REFUND_TICKET, MANAGE_SYSTEM_SETTINGS)
private val adminRules = Rules.entries.toSet() - excludedAdminRules.toSet()

private val auditorRules = setOf(
    VIEW_USERS, VIEW_BUSES, VIEW_BOOKINGS, VIEW_SCHEDULES, VIEW_ROUTES,
    VIEW_TRANSACTIONS, VIEW_REVENUE, VIEW_EXPENSES, VIEW_SALES, VIEW_SPECIFIC_REPORTS,
)

private val supervisorRules = setOf(
    VIEW_BUSES, VIEW_BOOKINGS, VIEW_SCHEDULES, VIEW_ROUTES, ASSIGN_DRIVER, ASSIGN_OPERATOR,
)

private val inspectorRules = setOf(VERIFY_TICKET)

private val operatorRules = setOf(INITIATE_PAYMENT, ISSUE_TICKET, VERIFY_TICKET, VIEW_BOOKINGS, CREATE_BOOKINGS, CANCEL_BOOKINGS)

private val clerkRules = setOf(INITIATE_PAYMENT, ISSUE_TICKET, CREATE_BOOKINGS, VIEW_BOOKINGS, CANCEL_BOOKINGS)

private val driverRules = setOf(VIEW_SCHEDULES, VIEW_ROUTES)