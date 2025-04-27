package com.example.risezonefitness.data.repository

import com.example.risezonefitness.model.Member
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.ktx.toObject
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FirestoreRepository @Inject constructor() {

    private val membersCollection = FirebaseFirestore.getInstance().collection("Members")

    fun getMembersFlow(): Flow<List<Member>> = callbackFlow {
        val listenerRegistration: ListenerRegistration =
            membersCollection.addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }

                val members = snapshot?.documents?.mapNotNull { document ->
                    document.toObject<Member>()?.apply {
                        username = document.id
                    }
                } ?: emptyList()

                if (!isClosedForSend) {
                    trySend(members).isSuccess
                }
            }

        awaitClose {
            listenerRegistration.remove()
        }
    }
}