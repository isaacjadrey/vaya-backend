package com.safarivaya.vayabackend.modules.auth.config

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "app.mail")
data class MailProperties(
    val frontendBaseUrl: String,
    val fromAddress: String,
    val appName: String
)
