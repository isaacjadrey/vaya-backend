package com.safarivaya.vayabackend.modules.fleet.repository

import com.safarivaya.vayabackend.modules.fleet.entity.Bus
import com.safarivaya.vayabackend.modules.fleet.entity.Seat
import org.springframework.data.jpa.repository.JpaRepository

interface BusRepository : JpaRepository<Bus, String> {
    fun findAllByTenantId(tenantId: String?): List<Bus>
    fun findByIdAndTenantId(id: String, tenantId: String?): Bus?
    fun existsByTenantIdAndLicensePlate(tenantId: String, licensePlate: String?): Boolean
}

interface SeatRepository : JpaRepository<Seat, String> {
    fun findAllByBusId(busId: String?): List<Seat>
}