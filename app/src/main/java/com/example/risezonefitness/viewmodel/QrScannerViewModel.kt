package com.example.risezonefitness.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.risezonefitness.model.Member
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class QrScannerViewModel : ViewModel() {

    private val db = FirebaseFirestore.getInstance()

    private val _memberState = MutableStateFlow<Member?>(null)
    val memberState: StateFlow<Member?> = _memberState

    private val _errorState = MutableStateFlow<String?>(null)
    val errorState: StateFlow<String?> = _errorState

    private var listenerRegistration: ListenerRegistration? = null

    fun listenToMember(username: String) {
        viewModelScope.launch {

            listenerRegistration?.remove()

            listenerRegistration = db.collection("Members")
                .whereEqualTo("username", username)
                .limit(1)
                .addSnapshotListener { snapshot, exception ->
                    if (exception != null) {
                        _errorState.value = exception.message
                        return@addSnapshotListener
                    }

                    if (snapshot != null && !snapshot.isEmpty) {
                        val document = snapshot.documents[0]
                        val member = document.toObject(Member::class.java)
                        member?.let {
                            _memberState.value = it
                        }
                    } else {
                        _errorState.value = "Member not found."
                    }
                }
        }
    }

    fun updateMemberInGymStatus(username: String, isInGym: Boolean) {
        viewModelScope.launch {
            db.collection("Members")
                .whereEqualTo("username", username)
                .limit(1)
                .get()
                .addOnSuccessListener { documents ->
                    if (!documents.isEmpty) {
                        val document = documents.documents[0]
                        val memberId = document.id
                        db.collection("Members").document(memberId)
                            .update("inGym", isInGym)
                            .addOnSuccessListener {
                            }
                            .addOnFailureListener { exception ->
                                _errorState.value = exception.message
                            }
                    } else {
                        _errorState.value = "Member not found."
                    }
                }
                .addOnFailureListener { exception ->
                    _errorState.value = exception.message
                }
        }
    }

    override fun onCleared() {
        super.onCleared()
        listenerRegistration?.remove()
    }
}
