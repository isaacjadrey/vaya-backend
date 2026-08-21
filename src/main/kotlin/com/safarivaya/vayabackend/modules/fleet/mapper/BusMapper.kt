package com.safarivaya.vayabackend.modules.fleet.mapper

import com.safarivaya.vayabackend.modules.fleet.dto.BusResponse
import com.safarivaya.vayabackend.modules.fleet.dto.SeatResponse
import com.safarivaya.vayabackend.modules.fleet.entity.Bus
import com.safarivaya.vayabackend.modules.fleet.entity.Seat

fun Seat.toResponse() = SeatResponse(id, seatNumber, deck, row, column, seatType, isActive)

fun Bus.toResponse(seats: List<Seat>) = BusResponse(
    id = id,
    busName = busName,
    licensePlate = licensePlate,
    busBrand = busBrand,
    totalSeats = totalSeats,
    layoutType = layoutType,
    status = status,
    statusReason = statusReason,
    amenities = amenities,
    seats = seats.map { it.toResponse() }
)