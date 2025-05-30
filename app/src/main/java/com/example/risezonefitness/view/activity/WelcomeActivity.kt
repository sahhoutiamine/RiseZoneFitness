package com.example.risezonefitness.view.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.airbnb.lottie.LottieAnimationView
import com.example.risezonefitness.R
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class WelcomeActivity : AppCompatActivity() {

    private lateinit var welcomeAnimation: LottieAnimationView
    private lateinit var loadingAnimation: LottieAnimationView
    private lateinit var wLoginButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcome)

        welcomeAnimation = findViewById(R.id.welcomeAnimation)
        loadingAnimation = findViewById(R.id.loadingAnimation)
        wLoginButton = findViewById(R.id.wloginbtn)

        welcomeAnimation.setRepeatCount(0)
        welcomeAnimation.playAnimation()

        welcomeAnimation.addAnimatorUpdateListener { animation ->
            if (animation.animatedFraction == 1f) {
                welcomeAnimation.pauseAnimation()
            }
        }

        wLoginButton.setOnClickListener {
            startLoadingAndFetchData()
        }
    }

    private fun startLoadingAndFetchData() {
        wLoginButton.isEnabled = false
        loadingAnimation.visibility = LottieAnimationView.VISIBLE
        loadingAnimation.playAnimation()

        lifecycleScope.launch {
            fetchDataFromFirestore()
        }
    }

    private suspend fun fetchDataFromFirestore() {
        val firestore = FirebaseFirestore.getInstance()

        try {
            firestore.collection("Members")
                .get()
                .addOnSuccessListener { result ->
                    lifecycleScope.launch {
                        delay(2500)
                        loadingAnimation.cancelAnimation()
                        loadingAnimation.visibility = LottieAnimationView.GONE
                        goToLoginActivity()
                    }
                }
                .addOnFailureListener { exception ->
                    lifecycleScope.launch {
                        loadingAnimation.cancelAnimation()
                        loadingAnimation.visibility = LottieAnimationView.GONE
                        wLoginButton.isEnabled = true
                        Toast.makeText(this@WelcomeActivity, "Data loading failed ", Toast.LENGTH_SHORT).show()
                    }
                }
        } catch (e: Exception) {
            loadingAnimation.cancelAnimation()
            loadingAnimation.visibility = LottieAnimationView.GONE
            wLoginButton.isEnabled = true
            Toast.makeText(this@WelcomeActivity, "An unexpected error occurred.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun goToLoginActivity() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }
}
