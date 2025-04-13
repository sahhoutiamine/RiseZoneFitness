package com.example.risezonefitness

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class AdminMainActivity : AppCompatActivity() {

    private lateinit var bottomNav: BottomNavigationView
    lateinit var allMembers: List<Member>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_main)

        bottomNav = findViewById(R.id.bottomNav)

        allMembers = listMembers

        openFragment(HomeFragment())

        val firstItem = bottomNav.menu.findItem(R.id.nav_home)
        firstItem.icon?.setTint(ContextCompat.getColor(this, R.color.primary_color))

        bottomNav.setOnItemSelectedListener { menuItem ->
            menuItem.icon?.setTint(ContextCompat.getColor(this, R.color.primary_color))


            when (menuItem.itemId) {
                R.id.nav_home -> {
                    openFragment(HomeFragment())
                    true
                }
                R.id.nav_members -> {
                    openFragment(MembersFragment(allMembers))
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

    private fun openFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, fragment)
            .commit()
    }



    var backPressedOnce = false

    override fun onBackPressed() {
        if (backPressedOnce) {
            super.onBackPressed()
        } else {
            backPressedOnce = true
            Toast.makeText(this, "Press again to exit", Toast.LENGTH_SHORT).show()

            Handler(Looper.getMainLooper()).postDelayed({
                backPressedOnce = false
            }, 2000)
        }
    }

}
