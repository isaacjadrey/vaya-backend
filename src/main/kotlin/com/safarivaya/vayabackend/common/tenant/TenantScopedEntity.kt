package com.safarivaya.vayabackend.common.tenant

import jakarta.persistence.Column
import jakarta.persistence.MappedSuperclass
import org.hibernate.annotations.Filter
import org.hibernate.annotations.FilterDef
import org.hibernate.annotations.ParamDef

@MappedSuperclass
@FilterDef(name = "tenantFilter", parameters =[ParamDef(name = "tenantId", type = String::class)])
@Filter(name = "tenantFilter", condition = "tenant_id = :tenantId")
abstract class TenantScopedEntity(
    @Column(name = "tenant_id", nullable = false)
    open var tenantId: String
)