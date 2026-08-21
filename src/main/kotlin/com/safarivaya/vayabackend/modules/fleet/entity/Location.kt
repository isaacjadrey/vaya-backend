package com.safarivaya.vayabackend.modules.fleet.entity

import com.safarivaya.vayabackend.common.tenant.TenantScopedEntity
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import org.hibernate.annotations.Filter
import org.hibernate.annotations.FilterDef
import org.hibernate.annotations.ParamDef
import java.time.LocalDateTime

@FilterDef(name = "tenantFilter", parameters = [ParamDef(name = "tenantId", type = String::class)])
@Filter(name = "tenantFilter", condition = "tenant_id = :tenantId")
@Entity
@Table(name = "locations")
class Location(
    @Id val id: String,
    tenantId: String,
    @Column(nullable = false) var name: String,
    var latitude: Double? = null,
    var longitude: Double? = null,
    @Column(nullable = false) val createdAt: LocalDateTime = LocalDateTime.now(),
) : TenantScopedEntity(tenantId)