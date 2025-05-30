package com.example.risezonefitness.view.fragment

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.airbnb.lottie.LottieAnimationView
import com.example.risezonefitness.viewmodel.AddMemberViewModel
import com.example.risezonefitness.view.activity.AdminMainActivity
import com.example.risezonefitness.R
import com.example.risezonefitness.model.Member
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
    private lateinit var profileImage: ImageView
    private lateinit var loadingAnimation: LottieAnimationView
    private var selectedImageUri: Uri? = null

    private val addMemberViewModel: AddMemberViewModel by viewModels()

    private val imagePickerLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK && result.data != null) {
                selectedImageUri = result.data?.data
                profileImage.setImageURI(selectedImageUri)
            }
        }

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
        profileImage = view.findViewById(R.id.profileImage)
        loadingAnimation = view.findViewById(R.id.loadingAnimation)


        profileImage.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            imagePickerLauncher.launch(intent)
        }

        saveButton.setOnClickListener {
            saveMember()
        }

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


        if (!fullName.isEmpty() || !age.isEmpty() || !gender.isEmpty() || !phone.isEmpty() ||
            !email.isEmpty() || !cin.isEmpty() || !username.isEmpty() || !password.isEmpty()
        ) {
            loadingAnimation.visibility = View.VISIBLE
            saveButton.isEnabled = false
            saveButton.visibility = View.INVISIBLE
        }else{
            Toast.makeText(requireContext(), "Please Fill the Fields Before !!", Toast.LENGTH_SHORT).show()
            return
        }

        val drawable = profileImage.drawable
        val memberImage = if (drawable is BitmapDrawable) drawable.bitmap else null

        val newMember = Member(
            fullName,
            age.toIntOrNull() ?: 0,
            gender,
            "+212$phone",
            email,
            cin,
            username,
            password,
            memberImage
        )

        addMemberViewModel.addNewMember(newMember,
            onSuccess = {
                loadingAnimation.visibility = View.GONE
                saveButton.isEnabled = true
                saveButton.visibility = View.VISIBLE
                showSuccessDialog()
                clearFields()
            },
            onFailure = { error ->
                loadingAnimation.visibility = View.GONE
                saveButton.isEnabled = true
                saveButton.visibility = View.VISIBLE
                Toast.makeText(requireContext(), "Failed to add member: $error", Toast.LENGTH_SHORT).show()
            }
        )
    }

    private fun showSuccessDialog() {
        val dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_member_added, null)
        val dialog = AlertDialog.Builder(requireContext())
            .setView(dialogView)
            .setCancelable(false)
            .create()

        val okButton = dialogView.findViewById<Button>(R.id.okButton)
        okButton.setOnClickListener {
            dialog.dismiss()
            requireActivity().supportFragmentManager.popBackStack()
        }

        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        dialog.show()
    }

    private fun clearFields() {
        fullNameInput.setText("")
        ageInput.setText("")
        genderInput.setText("")
        phoneInput.setText("")
        emailInput.setText("")
        cinInput.setText("")
        usernameInput.setText("")
        passwordInput.setText("")
        profileImage.setImageResource(R.drawable.ic_person)
        selectedImageUri = null
    }
}