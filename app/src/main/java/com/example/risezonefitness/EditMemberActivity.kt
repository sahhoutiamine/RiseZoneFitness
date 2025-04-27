package com.example.risezonefitness

import android.app.AlertDialog
import android.graphics.Bitmap
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.risezonefitness.databinding.ActivityEditMemberBinding
import com.example.risezonefitness.databinding.DialogConfirmSaveBinding
import com.example.risezonefitness.model.Member

class EditMemberActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEditMemberBinding
    private var selectedImageBitmap: Bitmap? = null
    private val editMemberViewModel: EditMemberViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityEditMemberBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val documentId = intent.getStringExtra("document_id")

        if (documentId != null) {
            editMemberViewModel.startListeningToMember(documentId)

            editMemberViewModel.memberLiveData.observe(this, { member ->
                member?.let {
                    binding.fullNameInput.setText(it.fullName)
                    binding.ageInput.setText(it.age.toString())
                    binding.genderInput.setText(it.gender)
                    binding.phoneInput.setText(it.phoneNumber)
                    binding.emailInput.setText(it.email)
                    binding.signUpCIN.setText(it.cin)
                    binding.usernameInput.setText(it.username)
                    binding.passwordInput.setText(it.password)

                    it.imageResource?.let {
                        binding.profileImage.setImageBitmap(it)
                        selectedImageBitmap = it
                    }
                }
            })

            binding.btnBack.setOnClickListener {
                onBackPressed()
            }

            binding.saveButton.setOnClickListener {
                val updatedMember = Member(
                    fullName = binding.fullNameInput.text.toString(),
                    age = binding.ageInput.text.toString().toIntOrNull() ?: 0,
                    gender = binding.genderInput.text.toString(),
                    phoneNumber = binding.phoneInput.text.toString(),
                    email = binding.emailInput.text.toString(),
                    cin = binding.signUpCIN.text.toString(),
                    username = binding.usernameInput.text.toString(),
                    password = binding.passwordInput.text.toString(),
                    isPaid = true,
                    isInGym = false,
                    imageResource = selectedImageBitmap
                )
                showConfirmDialog(updatedMember, documentId)
            }
        } else {
            Toast.makeText(this, "Invalid username", Toast.LENGTH_SHORT).show()
            finish()
        }
    }

    private fun showConfirmDialog(updatedMember: Member, documentId: String) {
        val dialogBinding = DialogConfirmSaveBinding.inflate(LayoutInflater.from(this))
        val dialogBuilder = AlertDialog.Builder(this)
            .setView(dialogBinding.root)
            .setCancelable(false)

        val alertDialog = dialogBuilder.create()
        alertDialog.window?.setBackgroundDrawableResource(android.R.color.transparent)

        dialogBinding.btnCancel.setOnClickListener {
            alertDialog.dismiss()
        }

        dialogBinding.btnConfirm.setOnClickListener {

            editMemberViewModel.updateMemberInFirestore(updatedMember, documentId)
            Toast.makeText(this, "Member updated successfully", Toast.LENGTH_SHORT).show()
            alertDialog.dismiss()
            finish()
        }

        alertDialog.show()
    }

    override fun onStop() {
        super.onStop()
        editMemberViewModel.stopListening()
    }
}
