package com.safarivaya.vayabackend.modules.company.repository

import com.safarivaya.vayabackend.modules.company.entities.Company
import com.safarivaya.vayabackend.modules.company.entities.CompanyAddress
import org.springframework.data.jpa.repository.JpaRepository

interface CompanyRepository : JpaRepository<Company, String> {
    fun existsBySlug(slug: String): Boolean
    fun existsByCompanyEmail(companyEmail: String): Boolean
}

interface CompanyAddressRepository : JpaRepository<CompanyAddress, String> {
    fun findAllByCompanyId(companyId: String): List<CompanyAddress>
}