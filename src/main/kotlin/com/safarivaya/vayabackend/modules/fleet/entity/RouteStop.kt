package com.safarivaya.vayabackend.modules.fleet.entity

import com.safarivaya.vayabackend.common.tenant.TenantScopedEntity
import jakarta.persistence.*
import org.hibernate.annotations.Filter
import org.hibernate.annotations.FilterDef
import org.hibernate.annotations.ParamDef
import java.math.BigDecimal
import java.time.LocalDateTime

@FilterDef(name = "tenantFilter", parameters = [ParamDef(name = "tenantId", type = String::class)])
@Filter(name = "tenantFilter", condition = "tenant_id = :tenantId")
@Entity
@Table(name = "route_stops")
class RouteStop(
    @Id val id: String,
    tenantId: String,
    @Column(nullable = false) val routeId: String,
    @Column(nullable = false) var stopLocationId: String,
    @Column(nullable = false) var stopPrice: BigDecimal,
    var stopDistanceKm: BigDecimal? = null,
    var stopDurationMinutes: Int? = null,
    @Column(nullable = false) var stopOrder: Int,
    @Column(nullable = false) var stopColor: String,
    @Column(nullable = false) val createdAt: LocalDateTime = LocalDateTime.now(),
) : TenantScopedEntity(tenantId)