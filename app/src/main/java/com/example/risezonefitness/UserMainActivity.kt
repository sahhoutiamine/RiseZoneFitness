package com.example.risezonefitness

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class UserMainActivity : AppCompatActivity() {

    private lateinit var bottomNav: BottomNavigationView
    private var username: String? = null
    private var backPressedOnce = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_main)

        bottomNav = findViewById(R.id.bottomNav)

        val toolbar = findViewById<Toolbar>(R.id.topAppBar)
        setSupportActionBar(toolbar)

        username = intent.getStringExtra("username")

        val sharedPref = getSharedPreferences("user_settings", MODE_PRIVATE)
        val lastFragment = sharedPref.getString("last_fragment", null)

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
        username?.let {
            val bundle = Bundle()
            bundle.putString("username", it)
            fragment.arguments = bundle
        }

        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, fragment)
            .commit()
    }

    fun updateToolbarTitle(title: String) {
        val toolbar = findViewById<Toolbar>(R.id.topAppBar)
        toolbar.title = title
    }
}
