package com.safarivaya.vayabackend.modules.fleet.service

import com.safarivaya.vayabackend.common.exception.ApplicationException
import com.safarivaya.vayabackend.common.id.IdGenerator
import com.safarivaya.vayabackend.modules.fleet.dto.CreateRouteRequest
import com.safarivaya.vayabackend.modules.fleet.dto.RouteResponse
import com.safarivaya.vayabackend.modules.fleet.dto.UpdateRouteStatusRequest
import com.safarivaya.vayabackend.modules.fleet.entity.Location
import com.safarivaya.vayabackend.modules.fleet.entity.Route
import com.safarivaya.vayabackend.modules.fleet.entity.RouteStop
import com.safarivaya.vayabackend.modules.fleet.mapper.toResponse
import com.safarivaya.vayabackend.modules.fleet.repository.LocationRepository
import com.safarivaya.vayabackend.modules.fleet.repository.RouteRepository
import com.safarivaya.vayabackend.modules.fleet.repository.RouteStopRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class RouteService(
    private val routeRepository: RouteRepository,
    private val routeStopRepository: RouteStopRepository,
    private val locationRepository: LocationRepository,
    private val locationService: LocationService,
    private val idGenerator: IdGenerator
) {

    @Transactional
    fun createRoute(tenantId: String, request: CreateRouteRequest): RouteResponse {
        val origin = locationService.resolve(tenantId, request.origin)
        val destination = locationService.resolve(tenantId, request.destination)

        val route = routeRepository.save(
            Route(
                id = idGenerator.next(),
                tenantId = tenantId,
                routeName = request.routeName,
                originLocationId = origin.id,
                destinationLocationId = destination.id,
                baseFare = request.baseFare,
                distanceKm = request.distanceKm,
                estDurationMinutes = request.estDurationMinutes
            )
        )

        val stops = request.stops.mapIndexed { index, stopRequest ->
            val stopLocation = locationService.resolve(tenantId, stopRequest.location)
            RouteStop(
                id = idGenerator.next(),
                tenantId = tenantId,
                routeId = route.id,
                stopLocationId = stopLocation.id,
                stopPrice = stopRequest.stopPrice,
                stopDistanceKm = stopRequest.stopDistanceKm,
                stopDurationMinutes = stopRequest.stopDurationMinutes,
                stopOrder = index + 1,
                stopColor = stopRequest.stopColor
            )
        }
        routeStopRepository.saveAll(stops)

        return buildResponse(route, origin, destination, stops)
    }

    fun getRoute(tenantId: String, routeId: String): RouteResponse {
        val route = routeRepository.findByIdAndTenantId(routeId, tenantId)
            ?: throw ApplicationException.NotFoundException("Route not found")
        val origin = locationRepository.findByIdAndTenantId(route.originLocationId, tenantId)!!
        val destination = locationRepository.findByIdAndTenantId(route.destinationLocationId, tenantId)!!
        val stops = routeStopRepository.findAllByRouteIdOrderByStopOrder(routeId)
        return buildResponse(route, origin, destination, stops)
    }

    fun listRoutes(tenantId: String): List<RouteResponse> =
        routeRepository.findAllByTenantId(tenantId).map { getRoute(tenantId, it.id) }

    @Transactional
    fun updateStatus(tenantId: String, routeId: String, request: UpdateRouteStatusRequest): RouteResponse {
        val route = routeRepository.findByIdAndTenantId(routeId, tenantId)
            ?: throw ApplicationException.NotFoundException("Route not found")
        route.isActive = request.isActive
        routeRepository.save(route)
        return getRoute(tenantId, routeId)
    }

    private fun buildResponse(
        route: Route,
        origin: Location,
        destination: Location,
        stops: List<RouteStop>
    ): RouteResponse {
        val stopLocations =
            stops.associateWith { locationRepository.findByIdAndTenantId(it.stopLocationId, route.tenantId)!! }
        return route.toResponse(origin, destination, stopLocations.map { (stop, loc) -> stop to loc })
    }
}