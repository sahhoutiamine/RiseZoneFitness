package com.example.risezonefitness

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.work.PeriodicWorkRequestBuilder
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.work.WorkManager
import com.example.risezonefitness.model.Member
import java.util.concurrent.TimeUnit

class AdminMainActivity : AppCompatActivity() {

    private lateinit var bottomNav: BottomNavigationView
    lateinit var allMembers: List<Member>
    private var backPressedOnce = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_main)

        bottomNav = findViewById(R.id.bottomNav)
        allMembers = listMembers

        val toolbar = findViewById<Toolbar>(R.id.topAppBar)
        setSupportActionBar(toolbar)

        val sharedPref = getSharedPreferences("user_settings", Context.MODE_PRIVATE)
        val lastFragment = sharedPref.getString("last_fragment", null)

        if (lastFragment == "settings") {
            openFragment(SettingsFragment())
            bottomNav.selectedItemId = R.id.nav_settings
            sharedPref.edit().remove("last_fragment").apply()
        } else {
            openFragment(HomeFragment())
            bottomNav.selectedItemId = R.id.nav_home
        }

        bottomNav.setOnItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.nav_home -> {
                    openFragment(HomeFragment())
                    true
                }
                R.id.nav_members -> {
                    openFragment(MembersFragment())
                    true
                }
                R.id.nav_add -> {
                    openFragment(AddFragment())
                    true
                }
                R.id.nav_settings -> {
                    openFragment(SettingsFragment())
                    true
                }
                else -> false
            }
        }

        schedulePaymentWorker()
    }
    override fun onResume() {
        super.onResume()
        val fragmentToLoad = intent.getStringExtra("fragmentToLoad")
        if (fragmentToLoad == "members") {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, MembersFragment())
                .commit()
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


    private fun schedulePaymentWorker() {
        val workRequest = PeriodicWorkRequestBuilder<PaymentCheckWorker>(
            3, TimeUnit.DAYS
        ).build()

        WorkManager.getInstance(this).enqueue(workRequest)
    }

    private fun openFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, fragment)
            .commit()
    }

    fun updateToolbarTitle(title: String) {
        val toolbar = findViewById<Toolbar>(R.id.topAppBar)
        toolbar.title = title
    }
}

