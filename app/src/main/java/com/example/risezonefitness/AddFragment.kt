package com.example.risezonefitness

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.android.material.textfield.TextInputEditText

class AddFragment : Fragment() {

    private lateinit var fullNameInput: TextInputEditText
    private lateinit var ageInput: TextInputEditText
    private lateinit var genderInput: TextInputEditText
    private lateinit var phoneInput: TextInputEditText
    private lateinit var emailInput: TextInputEditText
    private lateinit var cinInput: TextInputEditText
    private lateinit var usernameInput: TextInputEditText
    private lateinit var passwordInput: TextInputEditText
    private lateinit var saveButton: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_add, container, false)
        (activity as? AdminMainActivity)?.updateToolbarTitle("Add")


        fullNameInput = view.findViewById(R.id.fullNameInput)
        ageInput = view.findViewById(R.id.ageInput)
        genderInput = view.findViewById(R.id.genderInput)
        phoneInput = view.findViewById(R.id.phoneInput)
        emailInput = view.findViewById(R.id.emailInput)
        cinInput = view.findViewById(R.id.signUpCIN)
        usernameInput = view.findViewById(R.id.usernameInput)
        passwordInput = view.findViewById(R.id.passwordInput)
        saveButton = view.findViewById(R.id.saveButton)

        saveButton.setOnClickListener { saveMember() }

        return view
    }

    private fun saveMember() {
        val fullName = fullNameInput.text.toString().trim()
        val age = ageInput.text.toString().trim()
        val gender = genderInput.text.toString().trim()
        val phone = phoneInput.text.toString().trim()
        val email = emailInput.text.toString().trim()
        val cin = cinInput.text.toString().trim()
        val username = usernameInput.text.toString().trim()
        val password = passwordInput.text.toString().trim()

        if (fullName.isEmpty() || age.isEmpty() || gender.isEmpty() || phone.isEmpty() ||
            email.isEmpty() || cin.isEmpty() || username.isEmpty() || password.isEmpty()
        ) {
            Toast.makeText(requireContext(), "Please Fill the Fields Before !!", Toast.LENGTH_SHORT).show()
            return
        }


        val newMember = Member(
            fullName,
            age.toIntOrNull() ?: 0,
            gender,
            "+212$phone",
            email,
            cin,
            username,
            password,
            R.drawable.ic_person
        )


        listMembers.add(newMember)

        val dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_member_added, null)
        val dialog = AlertDialog.Builder(requireContext())
            .setView(dialogView)
            .setCancelable(false)
            .create()

        val okButton = dialogView.findViewById<Button>(R.id.okButton)
        okButton.setOnClickListener {
            dialog.dismiss()

            val rootView = requireView()

            rootView.findViewById<TextInputEditText>(R.id.fullNameInput).setText("")
            rootView.findViewById<TextInputEditText>(R.id.ageInput).setText("")
            rootView.findViewById<TextInputEditText>(R.id.genderInput).setText("")
            rootView.findViewById<TextInputEditText>(R.id.phoneInput).setText("")
            rootView.findViewById<TextInputEditText>(R.id.emailInput).setText("")
            rootView.findViewById<TextInputEditText>(R.id.usernameInput).setText("")
            rootView.findViewById<TextInputEditText>(R.id.passwordInput).setText("")
            rootView.findViewById<TextInputEditText>(R.id.signUpCIN).setText("")

            val profileImage = rootView.findViewById<ImageView>(R.id.profileImage)
            profileImage.setImageResource(R.drawable.ic_person)
        }

        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        dialog.show()

        requireActivity().supportFragmentManager.popBackStack()
    }
}
