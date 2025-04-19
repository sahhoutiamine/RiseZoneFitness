package com.example.risezonefitness

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.work.PeriodicWorkRequestBuilder
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.work.WorkManager
import java.util.concurrent.TimeUnit

class AdminMainActivity : AppCompatActivity() {

    private lateinit var bottomNav: BottomNavigationView
    lateinit var allMembers: List<Member>

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
                    openFragment(MembersFragment(allMembers))
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

