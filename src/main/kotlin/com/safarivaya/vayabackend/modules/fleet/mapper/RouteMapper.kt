package com.safarivaya.vayabackend.modules.fleet.mapper

import com.safarivaya.vayabackend.modules.fleet.dto.LocationResponse
import com.safarivaya.vayabackend.modules.fleet.dto.RouteResponse
import com.safarivaya.vayabackend.modules.fleet.dto.RouteStopResponse
import com.safarivaya.vayabackend.modules.fleet.entity.Location
import com.safarivaya.vayabackend.modules.fleet.entity.Route
import com.safarivaya.vayabackend.modules.fleet.entity.RouteStop

fun Location.toResponse() = LocationResponse(id, name, latitude, longitude)

fun Route.toResponse(origin: Location, destination: Location, stops: List<Pair<RouteStop, Location>>) = RouteResponse(
    id = id,
    routeName = routeName,
    origin = origin.toResponse(),
    destination = destination.toResponse(),
    baseFare = baseFare,
    distanceKm = distanceKm,
    estDurationMinutes = estDurationMinutes,
    isActive = isActive,
    stops = stops.sortedBy { it.first.stopOrder }.map { (stop, location) ->
        RouteStopResponse(
            stop.id,
            location.toResponse(),
            stop.stopPrice,
            stop.stopDistanceKm,
            stop.stopDurationMinutes,
            stop.stopOrder,
            stop.stopColor
        )
    }
)