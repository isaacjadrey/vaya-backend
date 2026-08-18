package com.safarivaya.vayabackend.modules.auth.service

import com.safarivaya.vayabackend.modules.auth.config.MailProperties
import org.springframework.mail.MailSender
import org.springframework.mail.SimpleMailMessage
import org.springframework.stereotype.Service

@Service
class SmtpMailService(
    private val mailSender: MailSender,
    private val properties: MailProperties
): MailService {
    override fun sendVerificationCode(to: String, code: String) {
        send(
            to = to,
            subject = "Verify your account",
            body = "Your verification code is: $code\nThis code expires in 15 minutes."
        )
    }

    private fun send(to: String, subject: String, body: String) {
        val message = SimpleMailMessage().apply {
            from = properties.fromAddress
            setTo(to)
            setSubject(subject)
            text = body
        }
        mailSender.send(message)
    }
}