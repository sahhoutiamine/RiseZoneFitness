package com.example.risezonefitness

import android.graphics.Bitmap

data class Member(
    val fullName: String,
    val age: Int,
    val gender: String,
    val phoneNumber: String,
    val email: String,
    val cin: String,
    val username: String,
    val password: String,
    val imageResource: Bitmap? = null,
    val isPaid: Boolean = true,
    val isInGym: Boolean = true,
    val registrationDate: Long = System.currentTimeMillis()
) {

    fun checkPaymentStatus(): Member {
        val oneMonthMillis = 30L * 24 * 60 * 60 * 1000
        val currentTime = System.currentTimeMillis()

        return if (currentTime - registrationDate >= oneMonthMillis && isPaid) {
            this.copy(isPaid = false)
        } else {
            this
        }
    }
}

fun addUnpaidMembers() {
    listMembers.forEach { member ->
        val updatedMember = member.checkPaymentStatus()
        if (!updatedMember.isPaid && !unpaidMembersList.contains(updatedMember)) {
            unpaidMembersList.add(updatedMember)
        }
    }
}

fun checkMembersAndNotify() {
    val senderEmail = "midoriaduko@gmail.com"
    val senderPassword = "rizezoneemail"
    val gmailSender = GmailSender(senderEmail, senderPassword)

    listMembers.forEach { member ->
        val updated = member.checkPaymentStatus()
        if (!updated.isPaid && member.isPaid) {
            val subject = "Your RiseZone Subscription Has Expired"

            val body = "Hello ${member.fullName},\n\nWe hope you're doing well.\n\nThis is a reminder that your subscription to RiseZone has expired.\nTo continue benefiting from our services, please renew your subscription at your earliest convenience.\n\nThank you for being part of RiseZone!"
            gmailSender.sendEmail(member.email, subject, body)
        }
    }
}

