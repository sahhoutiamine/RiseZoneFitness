package com.example.risezonefitness

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.airbnb.lottie.LottieAnimationView

class WelcomeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcone)

        val welcomeAnimation = findViewById<LottieAnimationView>(R.id.welcomeAnimation)
        val wLoginButton = findViewById<Button>(R.id.wloginbtn)

        welcomeAnimation.setRepeatCount(0)
        welcomeAnimation.playAnimation()

        welcomeAnimation.addAnimatorUpdateListener { animation ->
            if (animation.animatedFraction == 1f) {
                welcomeAnimation.pauseAnimation()
            }
        }

        wLoginButton.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
    }
}

