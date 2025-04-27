package com.example.risezonefitness


import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.risezonefitness.model.Member
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import kotlin.jvm.java
import kotlin.let


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
        db.collection("Members")
            .document(documentId)
            .set(updatedMember)
            .addOnSuccessListener {
            }
            .addOnFailureListener { exception ->
            }
    }

}
