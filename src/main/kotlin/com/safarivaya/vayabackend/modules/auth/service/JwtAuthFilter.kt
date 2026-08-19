package com.safarivaya.vayabackend.modules.auth.service

import com.safarivaya.vayabackend.common.tenant.TenantContext
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter

@Component
class JwtAuthFilter(private val jwtService: JwtService) : OncePerRequestFilter() {
    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        val header = request.getHeader("Authorization")
        if (header != null && header.startsWith("Bearer ")) {
            try {
                val claims = jwtService.parseAccessToken(header.removePrefix("Bearer "))
                val tenantId = claims["tenantId"] as? String
                val rules = (claims["permissions"] as? List<String>?)?.toSet() ?: emptySet()
                val principal = UserPrincipal(
                    userId = claims.subject,
                    email = claims["email"] as String,
                    role = claims["role"] as String,
                    tenantId = tenantId,
                    permissions = rules
                )
                val authorities = rules.map { SimpleGrantedAuthority(it) }
                val auth = UsernamePasswordAuthenticationToken(principal, null, authorities)
                SecurityContextHolder.getContext().authentication = auth

                tenantId?.let { TenantContext.set(it) }
            } catch (ex: Exception) {
                // invalid/expired token — leave unauthenticated, let downstream 401 naturally
            }
        }
        try {
            filterChain.doFilter(request, response)
        } finally {
            TenantContext.clear()
        }
    }
}