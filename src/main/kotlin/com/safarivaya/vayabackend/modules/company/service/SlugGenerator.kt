package com.safarivaya.vayabackend.modules.company.service

import com.safarivaya.vayabackend.modules.company.repository.CompanyRepository
import org.springframework.stereotype.Component

@Component
class SlugGenerator(private val companyRepository: CompanyRepository) {

    fun generateUnique(companyName: String): String {
        val base = companyName.lowercase()
            .replace(Regex("[^a-z0-9]"), "")
            .trim()
            .replace(Regex("\\s+"), "-")
            .ifBlank { "company" }

        if (!companyRepository.existsBySlug(base)) return base

        var suffix = 2
        while (companyRepository.existsBySlug("$base-$suffix")) suffix++

        return "$base-$suffix"
    }
}