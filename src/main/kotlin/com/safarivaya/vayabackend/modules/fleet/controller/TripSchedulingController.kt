package com.safarivaya.vayabackend.modules.fleet.controller

import com.safarivaya.vayabackend.modules.auth.service.UserPrincipal
import com.safarivaya.vayabackend.modules.fleet.dto.CreateTripScheduleRequest
import com.safarivaya.vayabackend.modules.fleet.dto.GenerateTripRequest
import com.safarivaya.vayabackend.modules.fleet.dto.TripResponse
import com.safarivaya.vayabackend.modules.fleet.dto.TripScheduleResponse
import com.safarivaya.vayabackend.modules.fleet.service.TripSchedulingService
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*
import java.time.LocalDate

@RestController
@RequestMapping("/fleets/scheduling")
class TripSchedulingController(private val service: TripSchedulingService) {

    @PreAuthorize("hasAuthority('CREATE_SCHEDULES')")
    @PostMapping("/schedules")
    fun createSchedule(
        @AuthenticationPrincipal principal: UserPrincipal,
        @Valid @RequestBody request: CreateTripScheduleRequest
    ): ResponseEntity<TripScheduleResponse> =
        ResponseEntity.status(HttpStatus.CREATED).body(service.createSchedule(principal.tenantId!!, request))

    @PreAuthorize("hasAuthority('CREATE_SCHEDULES')")
    @PostMapping("/schedules/{scheduleId}/generate-trips")
    fun generateTrips(
        @AuthenticationPrincipal principal: UserPrincipal,
        @PathVariable scheduleId: String,
        @Valid @RequestBody request: GenerateTripRequest
    ): ResponseEntity<List<TripResponse>> =
        ResponseEntity.ok(service.generateTrips(principal.tenantId!!, scheduleId, request))

    @PreAuthorize("hasAuthority('VIEW_SCHEDULES')")
    @GetMapping("/trips")
    fun listTrips(
        @AuthenticationPrincipal principal: UserPrincipal,
        @RequestParam date: LocalDate
    ): ResponseEntity<List<TripResponse>> =
        ResponseEntity.ok(service.listTrips(principal.tenantId!!, date))
}