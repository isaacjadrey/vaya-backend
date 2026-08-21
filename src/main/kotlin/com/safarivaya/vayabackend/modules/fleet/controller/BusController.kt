package com.safarivaya.vayabackend.modules.fleet.controller

import com.safarivaya.vayabackend.modules.auth.service.UserPrincipal
import com.safarivaya.vayabackend.modules.fleet.dto.BusResponse
import com.safarivaya.vayabackend.modules.fleet.dto.CreateBusRequest
import com.safarivaya.vayabackend.modules.fleet.dto.UpdateBusStatusRequest
import com.safarivaya.vayabackend.modules.fleet.service.BusService
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/fleet/buses")
class BusController(private val busService: BusService) {

    @PreAuthorize("hasAuthority('CREATE_BUS')")
    @PostMapping
    fun create(
        @AuthenticationPrincipal principal: UserPrincipal,
        @Valid @RequestBody request: CreateBusRequest
    ): ResponseEntity<BusResponse> =
        ResponseEntity.status(HttpStatus.CREATED).body(busService.createBus(principal.tenantId!!, request))

    @PreAuthorize("hasAuthority('VIEW_BUSES')")
    @GetMapping
    fun list(@AuthenticationPrincipal principal: UserPrincipal): ResponseEntity<List<BusResponse>> =
        ResponseEntity.ok(busService.listBuses(principal.tenantId!!))

    @PreAuthorize("hasAuthority('VIEW_BUSES')")
    @GetMapping("/{busId}")
    fun get(
        @AuthenticationPrincipal principal: UserPrincipal,
        @PathVariable busId: String
    ): ResponseEntity<BusResponse> =
        ResponseEntity.ok(busService.getBus(principal.tenantId!!, busId))

    @PreAuthorize("hasAuthority('EDIT_BUSES')")
    @PatchMapping("/{busId}/status")
    fun updateStatus(
        @AuthenticationPrincipal principal: UserPrincipal,
        @PathVariable busId: String,
        @Valid @RequestBody request: UpdateBusStatusRequest
    ): ResponseEntity<BusResponse> = ResponseEntity.ok(busService.updateStatus(principal.tenantId!!, busId, request))
}