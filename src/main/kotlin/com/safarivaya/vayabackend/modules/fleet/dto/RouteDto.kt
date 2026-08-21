package com.safarivaya.vayabackend.modules.fleet.dto

import jakarta.validation.Valid
import jakarta.validation.constraints.DecimalMin
import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotEmpty
import jakarta.validation.constraints.NotNull
import java.math.BigDecimal

data class LocationInput(
    val locationId: String? = null,
    val name: String? = null,
    val latitude: Double? = null,
    val longitude: Double? = null,
)

data class LocationResponse(
    val id: String,
    val name: String,
    val latitude: Double?,
    val longitude: Double?,
)

data class CreateRouteStopRequest(
    @Valid val location: LocationInput,
    @DecimalMin("0.0") val stopPrice: BigDecimal,
    val stopDistanceKm: BigDecimal? = null,
    val stopDurationMinutes: Int? = null,
    @NotBlank val stopColor: String
)

data class CreateRouteRequest(
    @field:NotBlank(message = "Route name is required")
    val routeName: String,
    @field:Valid
    @field:NotBlank(message = "Origin is required")
    val origin: LocationInput,
    @field:Valid
    @field:NotBlank(message = "Destination is required")
    val destination: LocationInput,
    @field:DecimalMin("0.0", inclusive = false)
    val baseFare: BigDecimal,
    val distanceKm: BigDecimal? = null,
    @field:NotNull @field:Min(1)
    val estDurationMinutes: Int,
    @field:Valid
    @field:NotEmpty(message = "At least one stop is required")
    val stops: List<CreateRouteStopRequest> = emptyList(),
)

data class UpdateRouteStatusRequest(val isActive: Boolean)

data class RouteStopResponse(
    val id: String,
    val location: LocationResponse,
    val stopPrice: BigDecimal,
    val stopDistanceKm: BigDecimal?,
    val stopDurationMinutes: Int?,
    val stopOrder: Int,
    val stopColor: String
)

data class RouteResponse(
    val id: String,
    val routeName: String,
    val origin: LocationResponse,
    val destination: LocationResponse,
    val baseFare: BigDecimal,
    val distanceKm: BigDecimal?,
    val estDurationMinutes: Int?,
    val isActive: Boolean,
    val stops: List<RouteStopResponse>
)