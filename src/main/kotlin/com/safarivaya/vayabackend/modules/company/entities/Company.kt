package com.safarivaya.vayabackend.modules.company.entities

import jakarta.persistence.*
import java.time.LocalDateTime

enum class CompanyStatus {
    ACTIVE, INACTIVE, SUSPENDED
}

@Entity
@Table(name = "companies")
class Company(
    @Id val id: String,
    @Column(nullable = false, unique = true) val slug: String,
    @Column(nullable = false) var companyName: String,
    @Column(nullable = false, unique = true) var companyEmail: String,
    @Column(nullable = false) var companyContact: String,
    var webUrl: String? = null,
    var logoUrl: String? = null,
    var themeConfig: String? = null,
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "company_operation_countries", joinColumns = [JoinColumn(name = "company_id")])
    @Column(name = "country_code")
    val operationCountries: MutableSet<String> = mutableSetOf(),
    @Enumerated(EnumType.STRING)
    var status: CompanyStatus = CompanyStatus.ACTIVE,
    @Column(nullable = false) val createdAt: LocalDateTime = LocalDateTime.now(),
)