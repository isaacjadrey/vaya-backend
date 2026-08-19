package com.safarivaya.vayabackend.common.tenant

import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.annotation.Before
import org.springframework.core.annotation.Order
import org.springframework.stereotype.Component

@Aspect
@Component
@Order(100)
class TenantFilterAspect(private val sessionManager: TenantSessionManager) {

    @Before("execution(public * com.safarivaya.vayabackend.modules..service..*.*(..))")
    fun applyTenantFilter() {
        val tenantId = TenantContext.get() ?: return
        sessionManager.activate(tenantId)
    }
}