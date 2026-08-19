package com.safarivaya.vayabackend.common.tenant

import jakarta.persistence.EntityManager
import jakarta.persistence.PersistenceContext
import org.hibernate.Session
import org.springframework.stereotype.Component

@Component
class TenantSessionManager {

    @PersistenceContext
    private lateinit var entityManager: EntityManager

    /** Activates tenant scoping for the remainder of the current transaction: Postgres RLS + the Hibernate filter, both. */
    fun activate(tenantId: String) {
        TenantContext.set(tenantId)

        entityManager.createNativeQuery("SELECT set_config('app.current.tenant_id', :tenantId, true)")
        .setParameter("tenantId", tenantId)
        .singleResult

        val session = entityManager.unwrap(Session::class.java)
        if (session.getEnabledFilter("tenant-filter") == null) session.enableFilter("tenant-filter").setParameter("tenantId", tenantId)
        println("Tenant filter enabled for tenant $tenantId")
    }
}