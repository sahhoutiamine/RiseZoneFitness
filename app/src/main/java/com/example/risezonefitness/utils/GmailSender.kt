package com.example.risezonefitness.utils

import java.util.Properties
import javax.mail.Authenticator
import javax.mail.Message
import javax.mail.PasswordAuthentication
import javax.mail.Session
import javax.mail.Transport
import javax.mail.internet.InternetAddress
import javax.mail.internet.MimeMessage

class GmailSender(
    private val email: String,
    private val password: String
) {

    fun sendEmail(toEmail: String, subject: String, message: String) {
        val props = Properties()
        props["mail.smtp.auth"] = "true"
        props["mail.smtp.starttls.enable"] = "true"
        props["mail.smtp.host"] = "smtp.gmail.com"
        props["mail.smtp.port"] = "587"

        val session = Session.getInstance(props, object : Authenticator() {
            override fun getPasswordAuthentication(): PasswordAuthentication {
                return PasswordAuthentication(email, password)
            }
        })

        try {
            val mimeMessage = MimeMessage(session)
            mimeMessage.setFrom(InternetAddress(email))
            mimeMessage.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail))
            mimeMessage.subject = subject
            mimeMessage.setText(message)

            Thread {
                Transport.send(mimeMessage)
            }.start()

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}