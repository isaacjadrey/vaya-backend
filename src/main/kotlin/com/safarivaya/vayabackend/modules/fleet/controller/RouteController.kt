package com.safarivaya.vayabackend.modules.fleet.controller

import com.safarivaya.vayabackend.modules.auth.service.UserPrincipal
import com.safarivaya.vayabackend.modules.fleet.dto.CreateRouteRequest
import com.safarivaya.vayabackend.modules.fleet.dto.RouteResponse
import com.safarivaya.vayabackend.modules.fleet.dto.UpdateRouteStatusRequest
import com.safarivaya.vayabackend.modules.fleet.service.RouteService
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/fleet/routing/routes")
class RouteController(private val service: RouteService) {

    @PreAuthorize("hasAuthority('CREATE_ROUTE')")
    @PostMapping
    fun create(
        @AuthenticationPrincipal principal: UserPrincipal,
        @Valid @RequestBody request: CreateRouteRequest
    ): ResponseEntity<RouteResponse> =
        ResponseEntity.status(HttpStatus.CREATED).body(service.createRoute(principal.tenantId!!, request))

    @PreAuthorize("hasAuthority('VIEW_ROUTES')")
    @GetMapping
    fun list(@AuthenticationPrincipal principal: UserPrincipal): ResponseEntity<List<RouteResponse>> =
        ResponseEntity.ok(service.listRoutes(principal.tenantId!!))

    @PreAuthorize("hasAuthority('VIEW_ROUTES')")
    @GetMapping("/{routeId}")
    fun get(
        @AuthenticationPrincipal principal: UserPrincipal,
        @PathVariable routeId: String
    ): ResponseEntity<RouteResponse> =
        ResponseEntity.ok(service.getRoute(principal.tenantId!!, routeId))

    @PreAuthorize("hasAuthority('DELETE_ROUTES')")
    @PatchMapping("/{routeId}/status")
    fun updateStatus(
        @AuthenticationPrincipal principal: UserPrincipal,
        @PathVariable routeId: String,
        @Valid @RequestBody request: UpdateRouteStatusRequest
    ): ResponseEntity<RouteResponse> =
        ResponseEntity.ok(service.updateStatus(principal.tenantId!!, routeId, request))
}