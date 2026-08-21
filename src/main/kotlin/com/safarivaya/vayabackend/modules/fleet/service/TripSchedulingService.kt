package com.safarivaya.vayabackend.modules.fleet.service

import com.safarivaya.vayabackend.common.exception.ApplicationException
import com.safarivaya.vayabackend.common.id.IdGenerator
import com.safarivaya.vayabackend.modules.fleet.dto.CreateTripScheduleRequest
import com.safarivaya.vayabackend.modules.fleet.dto.GenerateTripRequest
import com.safarivaya.vayabackend.modules.fleet.dto.TripResponse
import com.safarivaya.vayabackend.modules.fleet.dto.TripScheduleResponse
import com.safarivaya.vayabackend.modules.fleet.entity.Trip
import com.safarivaya.vayabackend.modules.fleet.entity.TripSchedule
import com.safarivaya.vayabackend.modules.fleet.entity.TripSeat
import com.safarivaya.vayabackend.modules.fleet.mapper.toResponse
import com.safarivaya.vayabackend.modules.fleet.repository.*
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDate

@Service
class TripSchedulingService(
    private val scheduleRepository: TripScheduleRepository,
    private val tripRepository: TripRepository,
    private val tripSeatRepository: TripSeatRepository,
    private val busRepository: BusRepository,
    private val routeRepository: RouteRepository,
    private val seatRepository: SeatRepository,
    private val idGenerator: IdGenerator
) {

    @Transactional
    fun createSchedule(tenantId: String, request: CreateTripScheduleRequest): TripScheduleResponse {
        val bus = busRepository.findByIdAndTenantId(request.busId, tenantId)
            ?: throw ApplicationException.NotFoundException("Bus not found")
        val route = routeRepository.findByIdAndTenantId(request.routeId, tenantId)
            ?: throw ApplicationException.NotFoundException("Route not found")
        if (!route.isActive) throw ApplicationException.ValidationException("Route is not active")
        val schedule = scheduleRepository.save(
            TripSchedule(
                id = idGenerator.next(),
                tenantId = tenantId,
                routeId = route.id,
                busId = bus.id,
                departureTime = request.departureTime,
                baseFare = request.baseFare,
                isRecurring = request.isRecurring,
                recurDays = request.recurDays.toMutableSet()
            )
        )

        return schedule.toResponse()
    }

    fun generateTrips(tenantId: String, scheduleId: String, request: GenerateTripRequest): List<TripResponse> {
        val schedule = scheduleRepository.findByIdAndTenantId(scheduleId, tenantId)
            ?: throw ApplicationException.NotFoundException("Schedule not found")
        if (!schedule.isActive) throw ApplicationException.NotFoundException("Schedule is not active")

        val route = routeRepository.findByIdAndTenantId(schedule.routeId, tenantId)
            ?: throw ApplicationException.NotFoundException("Route not found")
        val arrivalTime = schedule.departureTime.plusMinutes(route.estDurationMinutes.toLong())

        val busSeats = seatRepository.findAllByBusId(schedule.busId).filter { it.isActive }
        val results = mutableListOf<TripResponse>()

        var date = request.fromDate
        while (!date.isAfter(request.toDate)) {
            val matchersRecurrence = !schedule.isRecurring || schedule.recurDays.contains(date.dayOfWeek)
            if (matchersRecurrence && !tripRepository.existsByScheduleIdAndTripDate(scheduleId, date)) {
                val trip = tripRepository.save(
                    Trip(
                        id = idGenerator.next(),
                        tenantId = tenantId,
                        scheduleId = scheduleId,
                        busId = schedule.busId,
                        routeId = schedule.routeId,
                        tripDate = date,
                        departureTime = schedule.departureTime,
                        arrivalTime = arrivalTime,
                        fare = schedule.baseFare,
                    )
                )
                val tripSeats = busSeats.map {
                    TripSeat(
                        id = idGenerator.next(),
                        tenantId = tenantId,
                        tripId = trip.id,
                        seatId = it.id,
                        seatNumber = it.seatNumber,
                        fare = schedule.baseFare,
                    )
                }
                tripSeatRepository.saveAll(tripSeats)
                results.add(trip.toResponse(tripSeats))
            }
            date = date.plusDays(1)
        }

        return results
    }

    fun listTrips(tenantId: String, date: LocalDate): List<TripResponse> =
        tripRepository.findAllByTenantIdAndTripDate(tenantId, date)
            .map { it.toResponse(tripSeatRepository.findAllByTripId(it.id)) }
}