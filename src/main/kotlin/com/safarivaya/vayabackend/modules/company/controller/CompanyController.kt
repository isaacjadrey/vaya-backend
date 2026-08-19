package com.safarivaya.vayabackend.modules.company.controller

import com.safarivaya.vayabackend.common.exception.ApplicationException
import com.safarivaya.vayabackend.modules.auth.service.UserPrincipal
import com.safarivaya.vayabackend.modules.company.dto.CompanyCreationResponse
import com.safarivaya.vayabackend.modules.company.dto.CompanyResponse
import com.safarivaya.vayabackend.modules.company.dto.CreateCompanyRequest
import com.safarivaya.vayabackend.modules.company.service.CompanyService
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/company")
class CompanyController(private val service: CompanyService) {

    @PostMapping
    fun createCompany(
        @AuthenticationPrincipal principal: UserPrincipal,
        @Valid @RequestBody request: CreateCompanyRequest
    ): ResponseEntity<CompanyCreationResponse> = ResponseEntity.status(HttpStatus.CREATED).body(service.createCompany(principal.userId, request))

    @GetMapping("/me")
    fun getCompany(@AuthenticationPrincipal principal: UserPrincipal): ResponseEntity<CompanyResponse> {
        val tenantId = principal.tenantId ?: throw ApplicationException.NotFoundException("No company associated with this account")
        return ResponseEntity.ok(service.getCompany(tenantId))
    }
}