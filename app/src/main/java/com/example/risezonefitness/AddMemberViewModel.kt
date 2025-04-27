package com.example.risezonefitness


import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.risezonefitness.model.Member
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch
import kotlin.to


class AddMemberViewModel(application: Application) : AndroidViewModel(application) {

    private val db = FirebaseFirestore.getInstance()

    fun addNewMember(member: Member, onSuccess: () -> Unit, onFailure: (String) -> Unit) {
        val memberData = hashMapOf(
            "fullName" to member.fullName,
            "age" to member.age,
            "gender" to member.gender,
            "phoneNumber" to member.phoneNumber,
            "email" to member.email,
            "cin" to member.cin,
            "username" to member.username,
            "password" to member.password,
            "isPaid" to member.isPaid,
            "isInGym" to member.isInGym,
            "registrationDate" to member.registrationDate,
            "attendanceThisWeek" to member.attendanceThisWeek,
            "lastAttendanceReset" to member.lastAttendanceReset
        )

        viewModelScope.launch {
            db.collection("Members")
                .add(memberData)
                .addOnSuccessListener {
                    onSuccess()
                }
                .addOnFailureListener { exception ->
                    onFailure(exception.message ?: "Error adding member")
                }
        }
    }
}
