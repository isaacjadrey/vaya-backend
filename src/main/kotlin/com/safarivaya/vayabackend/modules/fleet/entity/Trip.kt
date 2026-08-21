package com.safarivaya.vayabackend.modules.fleet.entity

import com.safarivaya.vayabackend.common.tenant.TenantScopedEntity
import jakarta.persistence.*
import java.math.BigDecimal
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

enum class TripStatus { SCHEDULED, BOARDING, DEPARTED, COMPLETED, CANCELLED }
enum class SeatStatus { AVAILABLE, HELD, BOOKED, BLOCKED }

@Entity
@Table(name = "trip_schedules")
class TripSchedule(
    @Id val id: String,
    tenantId: String,
    @Column(nullable = false) val routeId: String,
    @Column(nullable = false) val busId: String,
    @Column(nullable = false) val departureTime: LocalTime,
    @Column(nullable = false) val baseFare: BigDecimal,
    var isRecurring: Boolean = true,
    var isActive: Boolean = true,
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "trip_schedule_recur_days", joinColumns = [JoinColumn("schedule_id")])
    @Enumerated(EnumType.STRING)
    @Column(name = "day_of_week")
    val recurDays: MutableSet<DayOfWeek> = mutableSetOf(),
    @Column(nullable = false) val createdAt: LocalDateTime = LocalDateTime.now()
) : TenantScopedEntity(tenantId)

@Entity
@Table(name = "trips")
class Trip(
    @Id val id: String,
    tenantId: String,
    @Column(nullable = false) val scheduleId: String,
    @Column(nullable = false) val busId: String,
    @Column(nullable = false) val routeId: String,
    @Column(nullable = false) val tripDate: LocalDate,
    @Column(nullable = false) val departureTime: LocalTime,
    @Column(nullable = false) val arrivalTime: LocalTime,
    @Column(nullable = false) val fare: BigDecimal,
    @Enumerated(EnumType.STRING) var status: TripStatus = TripStatus.SCHEDULED,
    @Column(nullable = false) val createdAt: LocalDateTime = LocalDateTime.now()
) : TenantScopedEntity(tenantId)

@Entity
@Table(name = "trip_seats")
class TripSeat(
    @Id val id: String,
    tenantId: String,
    @Column(nullable = false) val tripId: String,
    @Column(nullable = false) val seatId: String,
    @Column(nullable = false) val seatNumber: String,
    @Column(nullable = false) val fare: BigDecimal,
    @Enumerated(EnumType.STRING) var status: SeatStatus = SeatStatus.AVAILABLE,
    var heldByUserId: String? = null,
    var holdExpiresAt: LocalDateTime? = null,
) : TenantScopedEntity(tenantId)