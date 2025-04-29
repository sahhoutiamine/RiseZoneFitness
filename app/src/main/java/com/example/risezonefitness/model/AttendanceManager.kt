package com.example.risezonefitness.model

import com.google.firebase.firestore.FirebaseFirestore
import java.util.Calendar

object AttendanceManager {


    fun updateAttendanceIfNeeded(member: Member, onSuccess: () -> Unit = {}, onFailure: (String) -> Unit = {}) {

        val db = FirebaseFirestore.getInstance()
        val now = Calendar.getInstance()

        if (now.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
            onSuccess()
            return
        }

        val lastReset = Calendar.getInstance().apply {
            timeInMillis = member.lastAttendanceReset
        }

        val nowWeek = now.get(Calendar.WEEK_OF_YEAR)
        val nowYear = now.get(Calendar.YEAR)
        val lastWeek = lastReset.get(Calendar.WEEK_OF_YEAR)
        val lastYear = lastReset.get(Calendar.YEAR)

        var updatedAttendanceThisWeek = member.attendanceThisWeek
        var updatedLastAttendanceReset = member.lastAttendanceReset

        if (nowWeek != lastWeek || nowYear != lastYear) {
            updatedAttendanceThisWeek = 0
            updatedLastAttendanceReset = System.currentTimeMillis()

            db.collection("Members")
                .whereEqualTo("username", member.username)
                .limit(1)
                .get()
                .addOnSuccessListener { documents ->
                    if (!documents.isEmpty) {
                        val documentId = documents.documents[0].id
                        db.collection("Members").document(documentId)
                            .update(
                                mapOf(
                                    "attendanceThisWeek" to 0,
                                    "lastAttendanceReset" to updatedLastAttendanceReset
                                )
                            )
                    }
                }
        }

        val key = "${nowYear}-${nowWeek}-${now.get(Calendar.DAY_OF_WEEK)}"

        db.collection("AttendanceDays")
            .document(member.username)
            .get()
            .addOnSuccessListener { documentSnapshot ->
                val attendedDays = documentSnapshot.get("days") as? List<String> ?: emptyList()

                if (!attendedDays.contains(key)) {
                    val updatedDays = attendedDays.toMutableList()
                    updatedDays.add(key)

                    db.collection("AttendanceDays").document(member.username)
                        .set(mapOf("days" to updatedDays))
                        .addOnSuccessListener {
                            db.collection("Members")
                                .whereEqualTo("username", member.username)
                                .limit(1)
                                .get()
                                .addOnSuccessListener { documents ->
                                    if (!documents.isEmpty) {
                                        val documentId = documents.documents[0].id
                                        db.collection("Members").document(documentId)
                                            .update("attendanceThisWeek", updatedAttendanceThisWeek + 1)
                                            .addOnSuccessListener { onSuccess() }
                                            .addOnFailureListener { e -> onFailure(e.message ?: "Unknown error") }
                                    }
                                }
                        }
                        .addOnFailureListener { e ->
                            onFailure(e.message ?: "Unknown error")
                        }
                } else {
                    onSuccess()
                }
            }
            .addOnFailureListener { e ->
                onFailure(e.message ?: "Unknown error")
            }
    }
}
