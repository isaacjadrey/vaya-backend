package com.safarivaya.vayabackend.modules.fleet.mapper

import com.safarivaya.vayabackend.modules.fleet.dto.TripResponse
import com.safarivaya.vayabackend.modules.fleet.dto.TripScheduleResponse
import com.safarivaya.vayabackend.modules.fleet.entity.SeatStatus
import com.safarivaya.vayabackend.modules.fleet.entity.Trip
import com.safarivaya.vayabackend.modules.fleet.entity.TripSchedule
import com.safarivaya.vayabackend.modules.fleet.entity.TripSeat

fun TripSchedule.toResponse() = TripScheduleResponse(
    id = id,
    routeId = routeId,
    busId = busId,
    departureTime = departureTime,
    baseFare = baseFare,
    isRecurring = isRecurring,
    isActive = isActive,
    recurDays = recurDays
)

fun Trip.toResponse(seats: List<TripSeat>) = TripResponse(
    id = id,
    scheduleId = scheduleId,
    busId = busId,
    routeId = routeId,
    tripDate = tripDate,
    departureTime = departureTime,
    arrivalTime = arrivalTime,
    fare = fare,
    status = status,
    availableSeats = seats.count { it.status == SeatStatus.AVAILABLE },
    totalSeats = seats.size
)