package com.safarivaya.vayabackend.modules.fleet.dto

import com.safarivaya.vayabackend.modules.fleet.entity.TripStatus
import jakarta.validation.constraints.DecimalMin
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import java.math.BigDecimal
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.LocalTime

data class CreateTripScheduleRequest(
    @NotBlank val routeId: String,
    @NotBlank val busId: String,
    @NotBlank val departureTime: LocalTime,
    @DecimalMin("0.0", inclusive = false) val baseFare: BigDecimal,
    val isRecurring: Boolean = true,
    val recurDays: Set<DayOfWeek> = emptySet()
)

data class GenerateTripRequest(
    @NotNull val fromDate: LocalDate,
    @NotNull val toDate: LocalDate
)

data class TripScheduleResponse(
    val id: String,
    val routeId: String,
    val busId: String,
    val departureTime: LocalTime,
    val baseFare: BigDecimal,
    val isRecurring: Boolean,
    val isActive: Boolean,
    val recurDays: Set<DayOfWeek>
)

data class TripResponse(
    val id: String,
    val scheduleId: String,
    val busId: String,
    val routeId: String,
    val tripDate: LocalDate,
    val departureTime: LocalTime,
    val arrivalTime: LocalTime,
    val fare: BigDecimal,
    val status: TripStatus,
    val availableSeats: Int,
    val totalSeats: Int
)