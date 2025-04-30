package com.example.risezonefitness.utils

import android.util.Log
import com.example.risezonefitness.model.Member
import com.google.firebase.firestore.FirebaseFirestore

object MemberExtensions {

    fun checkAndUpdateMembersPaymentStatus() {

        val firestore = FirebaseFirestore.getInstance()
        val membersCollection = firestore.collection("Members")
        val unpaidMembersCollection = firestore.collection("UnpaidMembers")

        val oneMonthMillis = 30L * 24 * 60 * 60 * 1000
        val currentTime = System.currentTimeMillis()

        membersCollection.get().addOnSuccessListener { snapshot ->
            for (document in snapshot.documents) {
                val member = document.toObject(Member::class.java)
                val docId = document.id

                member?.let {
                    val shouldBeUnpaid = currentTime - it.registrationDate >= oneMonthMillis && it.isPaid
                    if (shouldBeUnpaid) {
                        val updatedMember = it.copy(isPaid = false)
                        membersCollection.document(docId).set(updatedMember)
                            .addOnSuccessListener {
                                Log.d("PaymentStatus", "Updated member ${updatedMember.fullName} to unpaid.")

                                unpaidMembersCollection.document(docId).get()
                                    .addOnSuccessListener { unpaidDoc ->
                                        if (!unpaidDoc.exists()) {
                                            unpaidMembersCollection.document(docId).set(updatedMember)
                                                .addOnSuccessListener {
                                                    Log.d("UnpaidMembers", "Added ${updatedMember.fullName} to UnpaidMembers collection.")
                                                }
                                                .addOnFailureListener { e ->
                                                    Log.e("UnpaidMembers", "Failed to add ${updatedMember.fullName} to UnpaidMembers: $e")
                                                }
                                        } else {
                                            Log.d("UnpaidMembers", "${updatedMember.fullName} is already in UnpaidMembers.")
                                        }
                                    }
                                    .addOnFailureListener { e ->
                                        Log.e("UnpaidMembers", "Error checking existence for ${updatedMember.fullName}: $e")
                                    }

                            }
                            .addOnFailureListener { e ->
                                Log.e("PaymentStatus", "Failed to update member ${updatedMember.fullName}: $e")
                            }
                    } else if (it.isPaid) {
                        unpaidMembersCollection.document(docId).delete()
                            .addOnSuccessListener {
                            }
                            .addOnFailureListener { e ->
                                Log.e("UnpaidMembers", "Failed to remove ${it.fullName} from UnpaidMembers: $e")
                            }
                    }
                }
            }
        }.addOnFailureListener {
            Log.e("PaymentStatus", "Failed to fetch members: $it")
        }
    }
}
