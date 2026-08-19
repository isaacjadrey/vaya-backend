package com.safarivaya.vayabackend.modules.company.entities

import jakarta.persistence.CollectionTable
import jakarta.persistence.Column
import jakarta.persistence.ElementCollection
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.OrderColumn
import jakarta.persistence.Table
import java.time.LocalDateTime

@Entity
@Table(name = "company_addresses")
class CompanyAddress(
    @Id val id: String,
    @Column(nullable = false) val companyId: String,
    @Column(nullable = false) val addressLabel: String,
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "company_address_lines", joinColumns = [JoinColumn(name = "address_id")])
    @OrderColumn(name = "line_order")
    @Column(name = "line")
    val addressLines: MutableList<String> = mutableListOf(),
    @Column(nullable = false) var addressLocation: String,
    @Column(nullable = false) var createdAt: LocalDateTime = LocalDateTime.now()
)