package com.example.risezonefitness

import android.app.AlertDialog
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class ProfileActivity : AppCompatActivity() {

    private lateinit var textFullName: TextView
    private lateinit var textAge: TextView
    private lateinit var textGender: TextView
    private lateinit var textStatus: TextView
    private lateinit var textPhoneNumber: TextView
    private lateinit var textEmail: TextView
    private lateinit var textCin: TextView
    private lateinit var textUsername: TextView
    private lateinit var textPassword: TextView
    private lateinit var textRegistrationDate: TextView
    private lateinit var textSubscriptionStatus: TextView
    private lateinit var btnBack: ImageButton
    private lateinit var btnEdit: ImageButton
    private lateinit var imgAvatar: ImageView
    private lateinit var btnDelete: ImageButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        textFullName = findViewById(R.id.textFullName)
        textAge = findViewById(R.id.textAge)
        textGender = findViewById(R.id.textGender)
        textStatus = findViewById(R.id.textInGym)
        textPhoneNumber = findViewById(R.id.text_number)
        textEmail = findViewById(R.id.text_email)
        textCin = findViewById(R.id.text_cin)
        textUsername = findViewById(R.id.text_username)
        textPassword = findViewById(R.id.text_pw)
        textRegistrationDate = findViewById(R.id.text_reg_date)
        textSubscriptionStatus = findViewById(R.id.textSubscriptionStatus)
        btnBack = findViewById(R.id.btnBack)
        btnEdit = findViewById(R.id.btnEdit)
        imgAvatar = findViewById(R.id.imgMemberAvatar)
        btnDelete = findViewById(R.id.btnDelete)

        val index = intent.getIntExtra("member_index", -1)
        if (index != -1) {
            val member = listMembers[index]

            textFullName.text = member.fullName
            textAge.text = member.age.toString()
            textGender.text = member.gender
            textStatus.text = if (member.isInGym) "In Gym" else "Not In Gym"
            textPhoneNumber.text = member.phoneNumber
            textEmail.text = member.email
            textCin.text = member.cin
            textUsername.text = member.username
            textPassword.text = member.password
            textRegistrationDate.text = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date(member.registrationDate))
            textSubscriptionStatus.text = if (member.isPaid) "Subscribed" else "Not Subscribed"
            textSubscriptionStatus.setTextColor(if (member.isPaid) Color.GREEN else Color.RED)

            if (member.imageResource != null) {
                imgAvatar.setImageBitmap(member.imageResource)
            } else {
                imgAvatar.setImageResource(R.drawable.ic_person)
            }

            btnEdit.setOnClickListener {
                val intent = Intent(this, EditMemberActivity::class.java)
                intent.putExtra("member_index", index)
                startActivity(intent)
            }

            btnDelete.setOnClickListener {
                val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_delete_confirmation, null)
                val dialog = AlertDialog.Builder(this)
                    .setView(dialogView)
                    .setCancelable(false)
                    .create()

                dialogView.findViewById<TextView>(R.id.btnCancel).setOnClickListener {
                    dialog.dismiss()
                }

                dialogView.findViewById<TextView>(R.id.btnDeleteConfirm).setOnClickListener {
                    listMembers.removeAt(index)
                    dialog.dismiss()
                    val intent = Intent(this, AdminMainActivity::class.java)
                    intent.putExtra("fragmentToLoad", "members")
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                    startActivity(intent)
                    finish()
                }

                dialog.show()
            }
        }

        btnBack.setOnClickListener {
            onBackPressed()
        }
    }
}
