package com.safarivaya.vayabackend.modules.company.dto

import com.safarivaya.vayabackend.modules.company.entities.CompanyStatus
import jakarta.validation.Valid
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotEmpty
import java.time.LocalDateTime

data class CreateCompanyRequest(
    @field:NotBlank(message = "Company name is required")
    val companyName: String,
    @field:NotBlank(message = "Company email is required")
    @field:Email
    val companyEmail: String,
    @field:NotBlank(message = "Company phone is required")
    val companyPhone: String,
    val webUrl: String? = null,
    @Valid
    @NotEmpty(message = "At least one country is required")
    val operationCountries: Set<String> = emptySet(),
    @field:Valid
    @field:NotEmpty(message = "At least one address is required")
    val addresses: List<CreateCompanyAddressRequest>
)

data class CreateCompanyAddressRequest(
    @field:NotBlank(message = "Address label is required")
    val addressLabel: String,
    @field:NotEmpty(message = "Address lines are required")
    val addressLines: List<String>,
    @field:NotBlank(message = "Address location is required")
    val addressLocation: String
)

data class CompanyAddressResponse(
    val id: String,
    val addressLabel: String,
    val addressLines: List<String>,
    val addressLocation: String
)

data class CompanyResponse(
    val id: String,
    val slug: String,
    val companyName: String,
    val companyEmail: String,
    val companyPhone: String,
    val webUrl: String?,
    val logoUrl: String?,
    val themeConfig: String?,
    val operationCountries: Set<String>,
    val status: CompanyStatus,
    val addresses: List<CompanyAddressResponse>,
    val createdAt: LocalDateTime
)

data class CompanyCreationResponse(
    val company: CompanyResponse,
    val accessToken: String,
    val refreshToken: String
)