package com.safarivaya.vayabackend.modules.fleet.dto

import com.safarivaya.vayabackend.modules.fleet.entity.Amenity
import com.safarivaya.vayabackend.modules.fleet.entity.BusStatus
import com.safarivaya.vayabackend.modules.fleet.entity.SeatType
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotEmpty

data class SeatLayoutRow(
    val seatNumber: String,
    val deck: Int = 1,
    val row: Int,
    val column: Int,
    val seatType: SeatType
)

data class CreateBusRequest(
    @field:NotBlank
    val busName: String,
    @field:NotBlank
    val licensePlate: String,
    @field:NotBlank
    val busBrand: String,
    @field:NotBlank
    val layoutType: String,
    val amenities: Set<Amenity> = emptySet(),
    @field:NotEmpty
    val seatLayout: List<SeatLayoutRow>,
)

data class UpdateBusStatusRequest(val status: BusStatus, val reason: String? = null)

data class SeatResponse(
    val id: String,
    val seatNumber: String,
    val deck: Int,
    val row: Int,
    val column: Int,
    val seatType: SeatType,
    val isActive: Boolean
)

data class BusResponse(
    val id: String,
    val busName: String,
    val licensePlate: String,
    val busBrand: String,
    val totalSeats: Int,
    val layoutType: String,
    val status: BusStatus,
    val statusReason: String?,
    val amenities: Set<Amenity>,
    val seats: List<SeatResponse>
)