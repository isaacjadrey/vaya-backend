package com.safarivaya.vayabackend.modules.fleet.repository

import com.safarivaya.vayabackend.modules.fleet.entity.Location
import com.safarivaya.vayabackend.modules.fleet.entity.Route
import com.safarivaya.vayabackend.modules.fleet.entity.RouteStop
import org.springframework.data.jpa.repository.JpaRepository

interface LocationRepository : JpaRepository<Location, String> {
    fun findByIdAndTenantId(id: String, tenantId: String?): Location?
    fun findByTenantIdAndNameIgnoreCase(tenantId: String, name: String): Location?
}

interface RouteRepository : JpaRepository<Route, String> {
    fun findAllByTenantId(tenantId: String): List<Route>
    fun findByIdAndTenantId(id: String, tenantId: String): Route?
}

interface RouteStopRepository : JpaRepository<RouteStop, String> {
    fun findAllByRouteIdOrderByStopOrder(routeId: String): List<RouteStop>
}