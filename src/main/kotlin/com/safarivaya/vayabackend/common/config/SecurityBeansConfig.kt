package com.safarivaya.vayabackend.common.config

import com.safarivaya.vayabackend.modules.auth.config.JwtProperties
import com.safarivaya.vayabackend.modules.auth.config.MailProperties
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder

@Configuration
@EnableConfigurationProperties(JwtProperties::class, MailProperties::class)
class SecurityBeansConfig {

    @Bean
    fun passwordEncoder(): PasswordEncoder = BCryptPasswordEncoder()
}