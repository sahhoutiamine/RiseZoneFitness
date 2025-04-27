package com.example.risezonefitness.viewmodel


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.risezonefitness.model.Member
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration

class UserProfileViewModel : ViewModel() {

    private val db = FirebaseFirestore.getInstance()

    private val _userProfile = MutableLiveData<Member>()
    val userProfile: LiveData<Member> = _userProfile

    private var listenerRegistration: ListenerRegistration? = null

    fun listenToUserProfile(username: String) {
        listenerRegistration?.remove()

        listenerRegistration = db.collection("Members")
            .whereEqualTo("username", username)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    return@addSnapshotListener
                }

                if (snapshot != null && !snapshot.isEmpty) {
                    val member = snapshot.documents.firstOrNull()?.toObject(Member::class.java)
                    member?.let {
                        _userProfile.postValue(it)
                    }
                }
            }
    }

    override fun onCleared() {
        super.onCleared()
        listenerRegistration?.remove()
    }
}
