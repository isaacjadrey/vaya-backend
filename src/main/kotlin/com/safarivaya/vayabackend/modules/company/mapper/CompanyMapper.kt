package com.safarivaya.vayabackend.modules.company.mapper

import com.safarivaya.vayabackend.modules.company.dto.CompanyAddressResponse
import com.safarivaya.vayabackend.modules.company.dto.CompanyResponse
import com.safarivaya.vayabackend.modules.company.entities.Company
import com.safarivaya.vayabackend.modules.company.entities.CompanyAddress

fun CompanyAddress.toResponse() = CompanyAddressResponse(id, addressLabel, addressLines, addressLocation)

fun Company.toResponse(addresses: List<CompanyAddress>) = CompanyResponse(
    id = id,
    slug = slug,
    companyName = companyName,
    companyEmail = companyEmail,
    companyPhone = companyContact,
    webUrl = webUrl,
    logoUrl = logoUrl,
    themeConfig = themeConfig,
    operationCountries = operationCountries,
    status = status,
    addresses = addresses.map { it.toResponse() },
    createdAt = createdAt
)