package com.safarivaya.vayabackend.modules.fleet.repository

import com.safarivaya.vayabackend.modules.fleet.entity.Trip
import com.safarivaya.vayabackend.modules.fleet.entity.TripSchedule
import com.safarivaya.vayabackend.modules.fleet.entity.TripSeat
import org.springframework.data.jpa.repository.JpaRepository
import java.time.LocalDate

interface TripScheduleRepository : JpaRepository<TripSchedule, String> {
    fun findAllByTenantId(tenantId: String) : List<TripSchedule>
    fun findByIdAndTenantId(id: String, tenantId: String): TripSchedule?
}

interface TripRepository : JpaRepository<Trip, String> {
    fun findByIdAndTenantId(id: String, tenantId: String) : Trip?
    fun existsByScheduleIdAndTripDate(scheduleId: String, date: LocalDate): Boolean
    fun findAllByTenantIdAndTripDate(tenantId: String, tipDate: LocalDate): List<Trip>
}

interface TripSeatRepository : JpaRepository<TripSeat, String> {
    fun findAllByTripId(tripId: String) : List<TripSeat>
}