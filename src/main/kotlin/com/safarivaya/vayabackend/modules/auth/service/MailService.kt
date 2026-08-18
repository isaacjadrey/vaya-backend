package com.safarivaya.vayabackend.modules.auth.service

interface MailService {
    fun sendVerificationCode(to: String, code: String)
}