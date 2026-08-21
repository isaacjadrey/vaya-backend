package com.safarivaya.vayabackend.modules.fleet.service

import com.safarivaya.vayabackend.common.exception.ApplicationException
import com.safarivaya.vayabackend.common.id.IdGenerator
import com.safarivaya.vayabackend.modules.fleet.dto.BusResponse
import com.safarivaya.vayabackend.modules.fleet.dto.CreateBusRequest
import com.safarivaya.vayabackend.modules.fleet.dto.UpdateBusStatusRequest
import com.safarivaya.vayabackend.modules.fleet.entity.Bus
import com.safarivaya.vayabackend.modules.fleet.entity.Seat
import com.safarivaya.vayabackend.modules.fleet.mapper.toResponse
import com.safarivaya.vayabackend.modules.fleet.repository.BusRepository
import com.safarivaya.vayabackend.modules.fleet.repository.SeatRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class BusService(
    private val busRepository: BusRepository,
    private val seatRepository: SeatRepository,
    private val idGenerator: IdGenerator
) {
    @Transactional
    fun createBus(tenantId: String, request: CreateBusRequest): BusResponse {
        if (busRepository.existsByTenantIdAndLicensePlate(tenantId, request.licensePlate)) {
            throw ApplicationException.ConflictException(
                "A bus with plate number ${request.licensePlate} already exists.",
                "PLATE_TAKEN"
            )
        }

        val bus = busRepository.save(
            Bus(
                id = idGenerator.next(),
                tenantId = tenantId,
                busName = request.busName,
                licensePlate = request.licensePlate,
                busBrand = request.busBrand,
                totalSeats = request.seatLayout.size,
                layoutType = request.layoutType,
                amenities = request.amenities.toMutableSet()
            )
        )
        val seats = request.seatLayout.map {
            Seat(
                id = idGenerator.next(),
                tenantId = tenantId,
                busId = bus.id,
                seatNumber = it.seatNumber,
                deck = it.deck,
                row = it.row,
                column = it.column,
                seatType = it.seatType
            )
        }
        seatRepository.saveAll(seats)

        return bus.toResponse(seats)
    }

    fun getBus(tenantId: String, busId: String): BusResponse {
        val bus = busRepository.findByIdAndTenantId(busId, tenantId)
            ?: throw ApplicationException.NotFoundException("Bus not found")
        return bus.toResponse(seatRepository.findAllByBusId(busId))
    }

    fun listBuses(tenantId: String): List<BusResponse> =
        busRepository.findAllByTenantId(tenantId).map { it.toResponse(seatRepository.findAllByBusId(it.id)) }

    @Transactional
    fun updateStatus(tenantId: String, busId: String, request: UpdateBusStatusRequest): BusResponse {
        val bus = busRepository.findByIdAndTenantId(busId, tenantId)
            ?: throw ApplicationException.NotFoundException("Bus not found")
        bus.status = request.status
        bus.statusReason = request.reason
        return busRepository.save(bus).toResponse(seatRepository.findAllByBusId(bus.id))
    }
}