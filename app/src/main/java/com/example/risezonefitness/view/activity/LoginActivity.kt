package com.example.risezonefitness.view.activity

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Button
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.lifecycleScope
import com.example.risezonefitness.viewmodel.LoginViewModel
import com.example.risezonefitness.R
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

class LoginActivity : AppCompatActivity() {

    private val viewModel: LoginViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        val sharedPref = getSharedPreferences("user_settings", MODE_PRIVATE)
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
            } else if (loginPassword.text.isNullOrEmpty()) {
                pwTextLayout.error = "Password is required"
                return@setOnClickListener
            } else {
                val username = loginUsername.text.toString().trim()
                val password = loginPassword.text.toString().trim()
                viewModel.login(username, password)
            }
        }

        lifecycleScope.launchWhenStarted {
            viewModel.loginState.collect { state ->
                when (state) {
                    is LoginViewModel.LoginState.Loading -> {
                    }
                    is LoginViewModel.LoginState.AdminSuccess -> {
                        startActivity(Intent(this@LoginActivity, AdminMainActivity::class.java))
                        finish()
                    }
                    is LoginViewModel.LoginState.MemberSuccess -> {
                        startActivity(Intent(this@LoginActivity, UserMainActivity::class.java))
                        finish()
                    }
                    is LoginViewModel.LoginState.Failure -> {
                        Toast.makeText(this@LoginActivity, state.message, Toast.LENGTH_SHORT).show()
                    }
                    else -> {}
                }
            }
        }
    }
}