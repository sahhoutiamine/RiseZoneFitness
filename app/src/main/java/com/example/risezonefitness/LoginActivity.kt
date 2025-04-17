package com.example.risezonefitness

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        val sharedPref = getSharedPreferences("user_settings", Context.MODE_PRIVATE)
        val themeMode = sharedPref.getInt("theme_mode", AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
        AppCompatDelegate.setDefaultNightMode(themeMode)

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)



        val pwTextLayout = findViewById<TextInputLayout>(R.id.pwLoginText)
        val usernameTextLayout = findViewById<TextInputLayout>(R.id.usernameLoginText)

        val loginUsername = findViewById<TextInputEditText>(R.id.loginUsername)
        val loginPassword = findViewById<TextInputEditText>(R.id.loginPassword)
        val loginBtn = findViewById<Button>(R.id.loginButton)

        loginUsername.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                usernameTextLayout.error = null
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        loginPassword.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                pwTextLayout.error = null
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        loginBtn.setOnClickListener {
            if (loginUsername.text.isNullOrEmpty()) {
                usernameTextLayout.error = "Username is required"
                return@setOnClickListener
            }
            else if (loginPassword.text.isNullOrEmpty()) {
                pwTextLayout.error = "Password is required"
                return@setOnClickListener
            }
            else {
                val username = loginUsername.text.toString()
                val password = loginPassword.text.toString()
                if (username == "admin" && password == "admin") {
                    val intent = Intent(this, AdminMainActivity::class.java)
                    startActivity(intent)
                }
                else {
                    pwTextLayout.error = "Invalid email or password"
                }
            }


        }
    }
}
