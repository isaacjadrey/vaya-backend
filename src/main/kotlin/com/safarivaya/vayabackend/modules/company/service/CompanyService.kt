package com.safarivaya.vayabackend.modules.company.service

import com.safarivaya.vayabackend.common.exception.ApplicationException
import com.safarivaya.vayabackend.common.id.IdGenerator
import com.safarivaya.vayabackend.common.tenant.TenantSessionManager
import com.safarivaya.vayabackend.modules.auth.dto.TokenPair
import com.safarivaya.vayabackend.modules.auth.entity.UserRole
import com.safarivaya.vayabackend.modules.auth.entity.UserStatus
import com.safarivaya.vayabackend.modules.auth.repository.UserRepository
import com.safarivaya.vayabackend.modules.auth.service.AuthService
import com.safarivaya.vayabackend.modules.company.dto.CompanyCreationResponse
import com.safarivaya.vayabackend.modules.company.dto.CompanyResponse
import com.safarivaya.vayabackend.modules.company.dto.CreateCompanyRequest
import com.safarivaya.vayabackend.modules.company.entities.Company
import com.safarivaya.vayabackend.modules.company.entities.CompanyAddress
import com.safarivaya.vayabackend.modules.company.mapper.toResponse
import com.safarivaya.vayabackend.modules.company.repository.CompanyAddressRepository
import com.safarivaya.vayabackend.modules.company.repository.CompanyRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class CompanyService(
    private val companyRepo: CompanyRepository,
    private val addressRepo: CompanyAddressRepository,
    private val userRepo: UserRepository,
    private val slugGenerator: SlugGenerator,
    private val authService: AuthService,
    private val idGenerator: IdGenerator,
    private val sessionManager: TenantSessionManager
) {
    @Transactional
    fun createCompany(userId: String, request: CreateCompanyRequest): CompanyCreationResponse {
        val user = userRepo.findById(userId).orElseThrow { ApplicationException.UnauthorizedException("Account no longer exists") }

        if (user.status != UserStatus.ACTIVE) {
            throw ApplicationException.UnauthorizedException("Account must be verified before creating a company", "ACCOUNT_NOT_VERIFIED")
        }
        if (user.tenantId != null) {
            throw ApplicationException.ConflictException("This account already belongs to a company", "COMPANY_ALREADY_EXISTS")
        }
        if (companyRepo.existsByCompanyEmail(request.companyEmail)) {
            throw ApplicationException.ConflictException("A company with this email already exists", "COMPANY_EMAIL_TAKEN")
        }

        val company = companyRepo.save(
            Company(
                id = idGenerator.next(),
                slug = slugGenerator.generateUnique(request.companyName),
                companyName = request.companyName,
                companyEmail = request.companyEmail,
                companyContact = request.companyPhone,
                webUrl = request.webUrl,
                operationCountries = request.operationCountries.toMutableSet(),
            )
        )

        sessionManager.activate(company.id)

        val addresses = request.addresses.map { address ->
            CompanyAddress(
                id = idGenerator.next(),
                companyId = company.id,
                addressLabel = address.addressLabel,
                addressLines = address.addressLines.toMutableList(),
                addressLocation = address.addressLocation
            )
        }
        addressRepo.saveAll(addresses)

        user.tenantId = company.id
        user.role = UserRole.SUPER_ADMIN
        userRepo.save(user)

        val tokens: TokenPair = authService.issueTokenPair(user)

        return CompanyCreationResponse(
            company = company.toResponse(addresses),
            accessToken = tokens.accessToken,
            refreshToken = tokens.refreshToken
        )
    }

    fun getCompany(companyId: String): CompanyResponse {
        val company = companyRepo.findById(companyId).orElseThrow { ApplicationException.NotFoundException("Company not found") }
        return company.toResponse(addressRepo.findAllByCompanyId(companyId))
    }
}