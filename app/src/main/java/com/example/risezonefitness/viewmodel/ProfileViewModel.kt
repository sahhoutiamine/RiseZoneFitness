package com.example.risezonefitness.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.risezonefitness.model.Member
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class ProfileViewModel : ViewModel() {

    private val db = FirebaseFirestore.getInstance()

    val memberLiveData = MutableLiveData<Member>()

    private var snapshotListener: ListenerRegistration? = null

    private val _deleteStatus = MutableStateFlow<Resource<Boolean>>(Resource.Loading())
    val deleteStatus: StateFlow<Resource<Boolean>> get() = _deleteStatus

    private val _updateStatus = MutableStateFlow<Resource<Boolean>>(Resource.Loading())
    val updateStatus: StateFlow<Resource<Boolean>> get() = _updateStatus


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

    fun deleteMember(documentId: String) {
        viewModelScope.launch {
            try {
                db.collection("Members")
                    .document(documentId)
                    .delete()
                    .await()

                _deleteStatus.value = Resource.Success(true)
            } catch (e: Exception) {
                _deleteStatus.value = Resource.Error("Error occurred while deleting: ${e.message}")
            }
        }
    }

    fun updateSubscriptionStatus(documentId: String, isPaid: Boolean) {
        viewModelScope.launch {
            try {
                db.collection("Members")
                    .document(documentId)
                    .update("paid", isPaid)
                    .await()


                _updateStatus.value = Resource.Success(true)
            } catch (e: Exception) {
                _updateStatus.value = Resource.Error("Error occurred while updating subscription: ${e.message}")
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
sealed class Resource<T> {
    data class Success<T>(val data: T) : Resource<T>()
    data class Error<T>(val message: String) : Resource<T>()
    class Loading<T> : Resource<T>()
}
