package com.safarivaya.vayabackend.modules.fleet.entity

import com.safarivaya.vayabackend.common.tenant.TenantScopedEntity
import jakarta.persistence.*
import java.time.LocalDateTime


enum class BusStatus { ACTIVE, MAINTENANCE, RETIRED }
enum class SeatType { STANDARD, PREMIUM, WINDOW, AISLE, MIDDLE, SLEEPER_LOWER, SLEEPER_UPPER }
enum class Amenity { WIFI, AC, USB_CHARGING, RECLINING_SEATS, TV, TOILET, BLANKET }

@Entity
@Table(name = "buses")
class Bus(
    @Id val id: String,
    tenantId: String,
    @Column(nullable = false) var busName: String,
    @Column(nullable = false) var licensePlate: String,
    @Column(nullable = false) var busBrand: String,
    @Column(nullable = false) var totalSeats: Int,
    @Column(nullable = false) var layoutType: String,
    @Enumerated(EnumType.STRING)
    var status: BusStatus = BusStatus.ACTIVE,
    var statusReason: String? = null,
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "bus_amenities", joinColumns = [JoinColumn(name = "bus_id")])
    @Enumerated(EnumType.STRING)
    @Column(name = "amenity")
    val amenities: MutableSet<Amenity> = mutableSetOf(),
    @Column(nullable = false) val createdAt: LocalDateTime = LocalDateTime.now()
) : TenantScopedEntity(tenantId)

@Entity
@Table(name = "seats")
class Seat(
    @Id val id: String,
    tenantId: String,
    @Column(nullable = false) var busId: String,
    @Column(nullable = false) var seatNumber: String,
    val deck: Int = 1,
    @Column(name = "row_no") val row: Int,
    @Column(name = "column_no") val column: Int,
    @Enumerated(EnumType.STRING) val seatType: SeatType,
    var isActive: Boolean = true
) : TenantScopedEntity(tenantId)