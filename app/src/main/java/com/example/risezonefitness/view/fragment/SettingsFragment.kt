package com.example.risezonefitness.view.fragment

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.core.content.edit
import androidx.fragment.app.Fragment
import com.example.risezonefitness.R
import com.example.risezonefitness.view.activity.WelcomeActivity
import com.example.risezonefitness.view.activity.AdminMainActivity
import java.util.Locale

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

    private fun showCustomListDialog(title: String, options: Array<String>, onSelect: (Int) -> Unit) {
        val dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_list_selection, null)
        val dialog = AlertDialog.Builder(requireContext())
            .setView(dialogView)
            .create()

        val tvTitle = dialogView.findViewById<TextView>(R.id.dialogTitle)
        val container = dialogView.findViewById<LinearLayout>(R.id.optionsContainer)

        tvTitle.text = title

        options.forEachIndexed { index, option ->
            val textView = TextView(requireContext()).apply {
                text = option
                textSize = 16f
                setTextColor(ContextCompat.getColor(requireContext(), R.color.black))
                setPadding(0, 24, 0, 24)
                gravity = Gravity.CENTER
                setOnClickListener {
                    onSelect(index)
                    dialog.dismiss()
                }
            }

            container.addView(textView)

            if (index != options.size - 1) {
                val divider = View(requireContext()).apply {
                    layoutParams = LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        1
                    )
                    setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.gray))
                }
                container.addView(divider)
            }
        }

        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        dialog.show()
    }


    private fun showThemeDialog() {
        val themes = arrayOf("Light", "Dark", "System Default")
        showCustomListDialog("Choose Theme", themes) { index ->
            val mode = when (index) {
                0 -> AppCompatDelegate.MODE_NIGHT_NO
                1 -> AppCompatDelegate.MODE_NIGHT_YES
                else -> AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
            }

            val sharedPref = requireContext().getSharedPreferences("user_settings", Context.MODE_PRIVATE)
            sharedPref.edit() {
                putInt("theme_mode", mode).putString("last_fragment", "settings")
            }

            AppCompatDelegate.setDefaultNightMode(mode)


            Toast.makeText(requireContext(), "Theme set to ${themes[index]}", Toast.LENGTH_SHORT).show()
        }
    }


    private fun showLanguageDialog() {
        val languages = arrayOf("العربية", "Français", "English")
        val languageCodes = arrayOf("ar", "fr", "en")

        showCustomListDialog("Choose Language", languages) { index ->
            val selectedLang = languageCodes[index]
            setLocale(selectedLang)

            val sharedPref = requireContext().getSharedPreferences("user_settings", Context.MODE_PRIVATE)
            sharedPref.edit {
                putString("app_lang", selectedLang)
                putString("last_fragment", "settings")
            }

            Toast.makeText(requireContext(), "Selected: ${languages[index]}", Toast.LENGTH_SHORT).show()

            requireActivity().recreate()
        }
    }


    private fun setLocale(languageCode: String) {
        val locale = Locale(languageCode)
        Locale.setDefault(locale)

        val config = Configuration()
        config.setLocale(locale)

        requireContext().resources.updateConfiguration(config, requireContext().resources.displayMetrics)
    }



    private fun logout() {
        val dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_logout_confirmation, null)
        val dialog = AlertDialog.Builder(requireContext())
            .setView(dialogView)
            .create()

        val tvYes = dialogView.findViewById<TextView>(R.id.tvYes)
        val tvNo = dialogView.findViewById<TextView>(R.id.tvNo)

        tvNo.setOnClickListener {
            dialog.dismiss()
        }

        tvYes.setOnClickListener {
            val sharedPref = requireContext().getSharedPreferences("user_settings", Context.MODE_PRIVATE)
            sharedPref.edit()
                .remove("is_logged_in")
                .remove("username")
                .remove("user_type")
                .apply()

            val intent = Intent(requireContext(), WelcomeActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)

            Toast.makeText(requireContext(), "Logged out", Toast.LENGTH_SHORT).show()
            dialog.dismiss()
        }

        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        dialog.show()
    }


}