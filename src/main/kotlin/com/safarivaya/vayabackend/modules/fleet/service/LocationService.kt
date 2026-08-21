package com.safarivaya.vayabackend.modules.fleet.service

import com.safarivaya.vayabackend.common.exception.ApplicationException
import com.safarivaya.vayabackend.common.id.IdGenerator
import com.safarivaya.vayabackend.modules.fleet.dto.LocationInput
import com.safarivaya.vayabackend.modules.fleet.entity.Location
import com.safarivaya.vayabackend.modules.fleet.repository.LocationRepository
import org.springframework.stereotype.Service

@Service
class LocationService(
    private val repository: LocationRepository,
    private val idGenerator: IdGenerator,
) {
    /** Resolves a LocationInput to a persisted Location: by id if given, else find-by-name-or-create. */
    fun resolve(tenantId: String, input: LocationInput): Location {
        input.locationId?.let { id ->
            return repository.findByIdAndTenantId(id, tenantId)
                ?: throw ApplicationException.NotFoundException("Location $id not found")
        }
        val name = input.name?.trim()
            ?: throw ApplicationException.ValidationException("Either locationId or name must be provided")

        repository.findByTenantIdAndNameIgnoreCase(tenantId, name)?.let { return it }

        return repository.save(Location(idGenerator.next(), tenantId, name, input.latitude, input.longitude))
    }
}