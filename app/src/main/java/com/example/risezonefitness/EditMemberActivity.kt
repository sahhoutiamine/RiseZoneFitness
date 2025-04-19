package com.example.risezonefitness



import android.app.Activity
import android.app.AlertDialog
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.risezonefitness.databinding.ActivityEditMemberBinding
import com.example.risezonefitness.databinding.ConfirmSaveDialogBinding

class EditMemberActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEditMemberBinding
    private var memberIndex: Int = -1
    private var selectedImageBitmap: Bitmap? = null

    private val imagePicker = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        uri?.let {
            val inputStream = contentResolver.openInputStream(uri)
            selectedImageBitmap = BitmapFactory.decodeStream(inputStream)
            binding.profileImage.setImageBitmap(selectedImageBitmap)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityEditMemberBinding.inflate(layoutInflater)
        setContentView(binding.root)

        memberIndex = intent.getIntExtra("member_index", -1)
        if (memberIndex == -1 || memberIndex >= listMembers.size) {
            Toast.makeText(this, "Invalid member", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        val member = listMembers[memberIndex]

        binding.fullNameInput.setText(member.fullName)
        binding.ageInput.setText(member.age.toString())
        binding.genderInput.setText(member.gender)
        binding.phoneInput.setText(member.phoneNumber)
        binding.emailInput.setText(member.email)
        binding.signUpCIN.setText(member.cin)
        binding.usernameInput.setText(member.username)
        binding.passwordInput.setText(member.password)

        member.imageResource?.let {
            binding.profileImage.setImageBitmap(it)
            selectedImageBitmap = it
        }

        binding.btnBack.setOnClickListener {
            onBackPressed()
        }

        binding.profileImage.setOnClickListener {
            imagePicker.launch("image/*")
        }

        binding.saveButton.setOnClickListener {
            showConfirmDialog(member)
        }
    }

    private fun showConfirmDialog(member: Member) {
        val dialogBinding = ConfirmSaveDialogBinding.inflate(LayoutInflater.from(this))
        val dialogBuilder = AlertDialog.Builder(this)
            .setView(dialogBinding.root)
            .setCancelable(false)

        val alertDialog = dialogBuilder.create()
        alertDialog.window?.setBackgroundDrawableResource(android.R.color.transparent)

        dialogBinding.btnCancel.setOnClickListener {
            alertDialog.dismiss()
        }

        dialogBinding.btnConfirm.setOnClickListener {
            val updatedMember = Member(
                fullName = binding.fullNameInput.text.toString(),
                age = binding.ageInput.text.toString().toIntOrNull() ?: 0,
                gender = binding.genderInput.text.toString(),
                phoneNumber = binding.phoneInput.text.toString(),
                email = binding.emailInput.text.toString(),
                cin = binding.signUpCIN.text.toString(),
                username = binding.usernameInput.text.toString(),
                password = binding.passwordInput.text.toString(),
                isPaid = member.isPaid,
                isInGym = member.isInGym,
                registrationDate = member.registrationDate,
                imageResource = selectedImageBitmap
            )

            listMembers[memberIndex] = updatedMember

            Toast.makeText(this, "Member updated successfully", Toast.LENGTH_SHORT).show()
            setResult(Activity.RESULT_OK)
            alertDialog.dismiss()
            finish()
        }

        alertDialog.show()
    }
}
