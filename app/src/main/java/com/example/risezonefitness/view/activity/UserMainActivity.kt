package com.example.risezonefitness.view.activity

import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import com.example.risezonefitness.R
import com.example.risezonefitness.view.fragment.UserProfileFragment
import com.example.risezonefitness.view.fragment.QrScannerFragment
import com.example.risezonefitness.view.fragment.SettingsFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import java.util.Locale

class UserMainActivity : AppCompatActivity() {

    private lateinit var bottomNav: BottomNavigationView
    private var username: String? = null
    private var backPressedOnce = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_main)

        bottomNav = findViewById(R.id.bottomNav)
        bottomNav.itemRippleColor = null
        bottomNav.itemIconTintList = null
        bottomNav.isItemActiveIndicatorEnabled = false

        val toolbar = findViewById<Toolbar>(R.id.topAppBar)
        setSupportActionBar(toolbar)

        username = intent.getStringExtra("username")

        val sharedPref = getSharedPreferences("user_settings", MODE_PRIVATE)
        val lastFragment = sharedPref.getString("last_fragment", null)
        loadLocale()

        if (lastFragment == "settings") {
            openFragment(SettingsFragment())
            bottomNav.selectedItemId = R.id.nav_settings
            sharedPref.edit().remove("last_fragment").apply()
        } else {
            openFragment(UserProfileFragment())
            bottomNav.selectedItemId = R.id.nav_profile
        }

        bottomNav.setOnItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.nav_profile -> {
                    openFragment(UserProfileFragment())
                    true
                }
                R.id.nav_qr_scanner -> {
                    openFragment(QrScannerFragment())
                    true
                }
                R.id.nav_settings -> {
                    openFragment(SettingsFragment())
                    true
                }
                else -> false
            }
        }
    }

    override fun onResume() {
        super.onResume()
        val fragmentToLoad = intent.getStringExtra("fragmentToLoad")
        if (fragmentToLoad == "members") {
            openFragment(QrScannerFragment())
            intent.removeExtra("fragmentToLoad")
        }
    }

    override fun onBackPressed() {
        if (backPressedOnce) {
            finishAffinity()
            return
        }

        this.backPressedOnce = true
        Toast.makeText(this, "Press again to exit", Toast.LENGTH_SHORT).show()

        Handler(Looper.getMainLooper()).postDelayed({
            backPressedOnce = false
        }, 2000)
    }

    private fun openFragment(fragment: Fragment) {
        val bundle = Bundle()
        bundle.putString("username", username)
        fragment.arguments = bundle

        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, fragment)
            .commit()
    }
    private fun loadLocale() {
        val prefs = getSharedPreferences("user_settings", Context.MODE_PRIVATE)
        val language = prefs.getString("app_lang", "en") ?: "en"

        val locale = Locale(language)
        Locale.setDefault(locale)

        val config = Configuration()
        config.setLocale(locale)

        resources.updateConfiguration(config, resources.displayMetrics)
    }

    fun updateToolbarTitle(title: String) {
        val toolbar = findViewById<Toolbar>(R.id.topAppBar)
        toolbar.title = title
    }
}
