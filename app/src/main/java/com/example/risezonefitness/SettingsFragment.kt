package com.example.risezonefitness


import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.core.content.edit

class SettingsFragment : Fragment(R.layout.fragment_settings) {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_settings, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        (activity as? AdminMainActivity)?.updateToolbarTitle("Settings")

        val themeCard = view.findViewById<CardView>(R.id.card_theme)
        val languageCard = view.findViewById<CardView>(R.id.card_language)
        val logoutCard = view.findViewById<CardView>(R.id.card_logout)


        themeCard.setOnClickListener {
            showThemeDialog()
        }

        languageCard.setOnClickListener {
            showLanguageDialog()
        }

        logoutCard.setOnClickListener {
            logout()
        }
    }

    private fun showThemeDialog() {
        val themes = arrayOf("Light", "Dark", "System Default")
        AlertDialog.Builder(requireContext())
            .setTitle("Choose Theme")
            .setItems(themes) { _, which ->
                Toast.makeText(requireContext(), "Selected: ${themes[which]}", Toast.LENGTH_SHORT).show()
            }
            .show()
    }

    private fun showLanguageDialog() {
        val languages = arrayOf("العربية", "Français", "English")
        AlertDialog.Builder(requireContext())
            .setTitle("Choose Language")
            .setItems(languages) { _, which ->
                Toast.makeText(requireContext(), "Selected: ${languages[which]}", Toast.LENGTH_SHORT).show()
            }
            .show()
    }
    private fun logout() {
        val sharedPref = requireContext().getSharedPreferences("user_session", Context.MODE_PRIVATE)
        sharedPref.edit() { clear() }

        val intent = Intent(requireContext(), LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)

        Toast.makeText(requireContext(), "Logged out", Toast.LENGTH_SHORT).show()
    }
}
