package com.example.risezonefitness.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class LoginViewModel(application: Application) : AndroidViewModel(application) {

    private val firestore = FirebaseFirestore.getInstance()
    private val sharedPreferences = application.getSharedPreferences("user_settings", Application.MODE_PRIVATE)

    private val _loginState = MutableStateFlow<LoginState>(LoginState.Idle)
    val loginState: StateFlow<LoginState> = _loginState

    fun login(username: String, password: String) {
        _loginState.value = LoginState.Loading

        viewModelScope.launch {
            firestore.collection("Admins")
                .whereEqualTo("username", username)
                .whereEqualTo("password", password)
                .get()
                .addOnSuccessListener { adminResult ->
                    if (!adminResult.isEmpty) {
                        saveUserInfo(username, "admin")
                        _loginState.value = LoginState.AdminSuccess(username)
                    } else {
                        firestore.collection("Members")
                            .whereEqualTo("username", username)
                            .whereEqualTo("password", password)
                            .get()
                            .addOnSuccessListener { memberResult ->
                                if (!memberResult.isEmpty) {
                                    saveUserInfo(username, "member")
                                    _loginState.value = LoginState.MemberSuccess(username)
                                } else {
                                    _loginState.value = LoginState.Failure("Incorrect username or password")
                                }
                            }
                            .addOnFailureListener {
                                _loginState.value = LoginState.Failure("Error: ${it.message}")
                            }
                    }
                }
                .addOnFailureListener {
                    _loginState.value = LoginState.Failure("Error: ${it.message}")
                }
        }
    }

    private fun saveUserInfo(username: String, userType: String) {
        sharedPreferences.edit()
            .putString("username", username)
            .putString("user_type", userType)
            .putBoolean("is_logged_in", true)
            .apply()
    }

    sealed class LoginState {
        object Loading : LoginState()
        data class AdminSuccess(val username: String) : LoginState()
        data class MemberSuccess(val username: String) : LoginState()
        data class Failure(val message: String) : LoginState()
        object Idle : LoginState()
    }

}
