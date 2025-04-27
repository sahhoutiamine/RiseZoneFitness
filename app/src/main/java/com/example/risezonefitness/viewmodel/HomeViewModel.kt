package com.example.risezonefitness.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await

class HomeViewModel : ViewModel() {

    private val db = FirebaseFirestore.getInstance()

    fun getFullName(username: String) = liveData(Dispatchers.IO) {
        val membersRef = db.collection("Admins")
        val member = membersRef.whereEqualTo("username", username).get().await().documents.firstOrNull()
        val fullName = member?.getString("fullName") ?: "User"
        emit(fullName)
    }

    val totalMembers = liveData(Dispatchers.IO) {
        val membersRef = db.collection("Members")
        val total = membersRef.get().await().size()
        emit(total)
    }

    val membersInGym = liveData(Dispatchers.IO) {
        val membersRef = db.collection("Members")
        val inGym = membersRef.whereEqualTo("inGym", true).get().await().size()
        emit(inGym)
    }

    val nonSubscribedMembers = liveData(Dispatchers.IO) {
        val membersRef = db.collection("Members")
        val nonSubscribed = membersRef.whereEqualTo("paid", false).get().await().size()
        emit(nonSubscribed)
    }
}
