package com.safarivaya.vayabackend.common.tenant

object TenantContext {
    private val current = ThreadLocal<String?>()

    fun set(companyId: String) { current.set(companyId)}
    fun get(): String? = current.get()
    fun require(): String = current.get()
        ?: throw IllegalStateException("No tenant set on the current request")
    fun clear() = current.remove()
}