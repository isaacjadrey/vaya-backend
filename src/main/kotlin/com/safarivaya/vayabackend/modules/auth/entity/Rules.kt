package com.safarivaya.vayabackend.modules.auth.entity

enum class Rules {
    // User Management
    CREATE_USERS, EDIT_USERS, DELETE_USERS, VIEW_USERS, ASSIGN_ROLES,
    // Fleet Management
    CREATE_BUSES, EDIT_BUSES, DELETE_BUSES, VIEW_BUSES,
    // Route Management
    CREATE_ROUTES, EDIT_ROUTES, DELETE_ROUTES, VIEW_ROUTES,
    // Booking Management
    CREATE_BOOKINGS, EDIT_BOOKINGS, CANCEL_BOOKINGS, VIEW_BOOKINGS,
    // Schedule Management
    CREATE_SCHEDULES, EDIT_SCHEDULES, DELETE_SCHEDULES, VIEW_SCHEDULES,
    // Financial Management
    INITIATE_PAYMENT, REFUND_PAYMENT, VIEW_TRANSACTIONS, VIEW_REVENUE, VIEW_EXPENSES, VIEW_SALES,
    // Ticket Management
    ISSUE_TICKET, VERIFY_TICKET, REFUND_TICKET,
    // Analytics Management
    VIEW_SPECIFIC_REPORTS,
    // Promotions Management
    CREATE_PROMOTION, EDIT_PROMOTION, DELETE_PROMOTION, VIEW_PROMOTION,
    // Driver Management
    ASSIGN_DRIVER,
    // Operator Management
    ASSIGN_OPERATOR,
    // System Settings
    MANAGE_SYSTEM_SETTINGS,
}