package com.example.risezonefitness.model

import android.graphics.Bitmap
import com.example.risezonefitness.utils.GmailSender
import com.example.risezonefitness.data.listMembers
import com.example.risezonefitness.data.unpaidMembersList
import com.google.firebase.firestore.Exclude
import com.google.firebase.firestore.IgnoreExtraProperties

@IgnoreExtraProperties
data class Member(
    var fullName: String = "",
    var age: Int = 0,
    var gender: String = "",
    var phoneNumber: String = "",
    var email: String = "",
    var cin: String = "",
    var username: String = "",
    var password: String = "",
    @get:Exclude var imageResource: Bitmap? = null,
    var isPaid: Boolean = true,
    var isInGym: Boolean = false,
    var registrationDate: Long = System.currentTimeMillis(),
    var attendanceThisWeek: Int = 0,
    var lastAttendanceReset: Long = System.currentTimeMillis()
)  {

    constructor() : this(
        "", 0, "", "", "", "", "", "", null, true, false,
        System.currentTimeMillis(), 0, System.currentTimeMillis()
    )

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

