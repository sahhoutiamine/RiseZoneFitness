package com.example.risezonefitness.utils



import com.example.risezonefitness.model.Member
import com.google.firebase.firestore.FirebaseFirestore

object PaymentNotify {


    fun notifyUnpaidMembers() {

        val firestore = FirebaseFirestore.getInstance()
        val unpaidCollection = firestore.collection("UnpaidMembers")


        val senderEmail = "midoriaduko@gmail.com"
        val senderPassword = "riseZoneSentEmail"
        val gmailSender = GmailSender(senderEmail, senderPassword)

        unpaidCollection.get().addOnSuccessListener { snapshot ->
            snapshot.documents.forEach { doc ->
                val member = doc.toObject(Member::class.java)
                member?.let {
                    val subject = "Your RiseZone Subscription Has Expired"
                    val body = """
                        Hello ${it.fullName},

                        We hope you're doing well.

                        This is a reminder that your subscription to RiseZone has expired.
                        To continue benefiting from our services, please renew your subscription at your earliest convenience.

                        Thank you for being part of RiseZone!
                    """.trimIndent()
                    gmailSender.sendEmail(it.email, subject, body)
                }
            }
        }
    }
}
