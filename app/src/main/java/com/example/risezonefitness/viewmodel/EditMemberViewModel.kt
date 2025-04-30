package com.example.risezonefitness.viewmodel

import android.app.Application
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.risezonefitness.model.Member
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration

class EditMemberViewModel(application: Application) : AndroidViewModel(application) {

    private val db = FirebaseFirestore.getInstance()

    val memberLiveData = MutableLiveData<Member>()
    private var snapshotListener: ListenerRegistration? = null

    fun startListeningToMember(documentId: String) {
        snapshotListener = db.collection("Members")
            .document(documentId)
            .addSnapshotListener { snapshot, e ->
                if (e != null) {
                    return@addSnapshotListener
                }

                snapshot?.let {
                    val member = it.toObject(Member::class.java)
                    member?.let {
                        memberLiveData.value = it
                    }
                }
            }
    }

    fun stopListening() {
        snapshotListener?.remove()
    }

    fun updateMemberInFirestore(updatedMember: Member, documentId: String) {
        val updatedFields = mapOf(
            "fullName" to updatedMember.fullName,
            "age" to updatedMember.age,
            "gender" to updatedMember.gender,
            "phoneNumber" to updatedMember.phoneNumber,
            "email" to updatedMember.email,
            "cin" to updatedMember.cin,
            "username" to updatedMember.username,
            "password" to updatedMember.password
        )

        db.collection("Members")
            .document(documentId)
            .update(updatedFields)
            .addOnSuccessListener {
                Log.d("FirestoreUpdate", "Member $documentId updated successfully.")

            }
            .addOnFailureListener { e ->
                Toast.makeText(getApplication(), "Failed to update member", Toast.LENGTH_SHORT).show()
                Log.e("FirestoreUpdate", "Error updating member $documentId", e)
            }
    }


}