package com.example.risezonefitness

import androidx.lifecycle.ViewModel
import androidx.lifecycle.MutableLiveData
import com.example.risezonefitness.model.Member
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine
import kotlin.jvm.java
import kotlin.let

class ProfileViewModel : ViewModel() {

    private val db = FirebaseFirestore.getInstance()

    val memberLiveData = MutableLiveData<Member>()

    private var snapshotListener: ListenerRegistration? = null

    fun startListeningToMemberProfile(username: String) {
        snapshotListener = db.collection("Members")
            .whereEqualTo("username", username)
            .addSnapshotListener { snapshots, e ->
                if (e != null) {
                    return@addSnapshotListener
                }

                if (snapshots != null && !snapshots.isEmpty) {
                    val documentSnapshot = snapshots.documents[0]
                    val member = documentSnapshot.toObject(Member::class.java)
                    member?.let {
                        memberLiveData.value = it
                    }
                }
            }
    }



    suspend fun getMemberDataOnce(username: String): Member? {
        return suspendCoroutine { continuation ->
            db.collection("Members")
                .whereEqualTo("username", username)
                .addSnapshotListener { querySnapshot, e ->
                    if (e != null) {
                        continuation.resume(null)
                        return@addSnapshotListener
                    }
                    if (querySnapshot != null && !querySnapshot.isEmpty) {
                        val documentSnapshot = querySnapshot.documents[0]
                        val member = documentSnapshot.toObject(Member::class.java)
                        continuation.resume(member)
                    } else {
                        continuation.resume(null)
                    }
                }
        }

    }
}
