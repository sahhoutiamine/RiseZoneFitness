package com.example.risezonefitness.utils

import android.util.Log
import com.example.risezonefitness.model.Member
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import java.util.Calendar

object MemberExtensions {

    fun checkAndUpdateMembersPaymentStatus() {
        val firestore = FirebaseFirestore.getInstance()
        val membersCollection = firestore.collection("Members")
        val unpaidMembersCollection = firestore.collection("UnpaidMembers")

        val currentTime = System.currentTimeMillis()
        val calendar = Calendar.getInstance().apply { timeInMillis = currentTime }

        membersCollection.get().addOnSuccessListener { snapshot ->
            for (document in snapshot.documents) {
                val member = document.toObject(Member::class.java)
                val docId = document.id

                member?.let {
                    val registrationCalendar = Calendar.getInstance().apply {
                        timeInMillis = it.registrationDate
                    }

                    val monthsDifference = getMonthsDifference(registrationCalendar, calendar)

                    val shouldBeUnpaid = monthsDifference >= 1 && it.isPaid

                    if (shouldBeUnpaid) {
                        val updatedMember = it.copy(isPaid = false)
                        updateMemberStatus(
                            membersCollection,
                            unpaidMembersCollection,
                            docId,
                            updatedMember
                        )
                    } else if (it.isPaid) {
                        unpaidMembersCollection.document(docId).delete()
                            .addOnSuccessListener {
                            }
                            .addOnFailureListener { e ->
                                Log.e("UnpaidMembers", "Error removing member: $e")
                            }
                    }
                }
            }
        }.addOnFailureListener {
            Log.e("PaymentStatus", "Failed to fetch members: $it")
        }
    }

    private fun getMonthsDifference(startDate: Calendar, endDate: Calendar): Int {
        val yearsDifference = endDate.get(Calendar.YEAR) - startDate.get(Calendar.YEAR)
        return yearsDifference * 12 + (endDate.get(Calendar.MONTH) - startDate.get(Calendar.MONTH))
    }

    private fun updateMemberStatus(
        membersCollection: CollectionReference,
        unpaidMembersCollection: CollectionReference,
        docId: String,
        updatedMember: Member
    ) {
        membersCollection.document(docId).set(updatedMember)
            .addOnSuccessListener {
                Log.d("PaymentStatus", "Updated ${updatedMember.fullName} to unpaid")

                unpaidMembersCollection.document(docId).get()
                    .addOnSuccessListener { unpaidDoc ->
                        if (!unpaidDoc.exists()) {
                            unpaidMembersCollection.document(docId).set(updatedMember)
                                .addOnSuccessListener {
                                    Log.d("UnpaidMembers", "Added ${updatedMember.fullName} to UnpaidMembers")
                                }
                                .addOnFailureListener { e ->
                                    Log.e("UnpaidMembers", "Failed to add: $e")
                                }
                        }
                    }
            }
            .addOnFailureListener { e ->
                Log.e("PaymentStatus", "Failed to update member: $e")
            }
    }
}