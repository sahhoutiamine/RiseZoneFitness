package com.example.risezonefitness

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import java.text.SimpleDateFormat
import java.util.*

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
    private lateinit var btnEmail: ImageButton
    private lateinit var btnSubscribed: Button

    private val profileViewModel: ProfileViewModel by viewModels()

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
        textUsername = findViewById(R.id.text_week_streak)
        textPassword = findViewById(R.id.text_last_session)
        textRegistrationDate = findViewById(R.id.text_reg_date)
        textSubscriptionStatus = findViewById(R.id.textSubscriptionStatus)
        btnBack = findViewById(R.id.btnBack)
        btnEdit = findViewById(R.id.btnEdit)
        imgAvatar = findViewById(R.id.imgMemberAvatar)
        btnDelete = findViewById(R.id.btnDelete)
        btnEmail = findViewById(R.id.btnEmail)
        btnSubscribed = findViewById(R.id.btnSubscribed)

        val username = intent.getStringExtra("member_username")
        val documentId = intent.getStringExtra("document_id")
        if (username != null) {
            profileViewModel.startListeningToMemberProfile(username)

            profileViewModel.memberLiveData.observe(this, Observer { member ->
                member?.let {
                    textFullName.text = it.fullName
                    textAge.text = it.age.toString()
                    textGender.text = it.gender
                    textStatus.text = if (it.isInGym) "In Gym" else "Not In Gym"
                    textPhoneNumber.text = it.phoneNumber
                    textEmail.text = it.email
                    textCin.text = it.cin
                    textUsername.text = it.username
                    textPassword.text = it.password
                    textRegistrationDate.text = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date(it.registrationDate))
                    textSubscriptionStatus.text = if (it.isPaid) "Subscribed" else "Not Subscribed"
                    textSubscriptionStatus.setTextColor(if (it.isPaid) Color.GREEN else Color.RED)

                    if (it.imageResource != null) {
                        imgAvatar.setImageBitmap(it.imageResource)
                    } else {
                        imgAvatar.setImageResource(R.drawable.ic_person)
                    }

                    btnEdit.setOnClickListener {
                        val intent = Intent(this, EditMemberActivity::class.java)
                        intent.putExtra("document_id", documentId)
                        startActivity(intent)
                    }

//                    btnDelete.setOnClickListener {
//                        val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_delete_confirmation, null)
//                        val dialog = AlertDialog.Builder(this)
//                            .setView(dialogView)
//                            .setCancelable(false)
//                            .create()
//
//                        dialogView.findViewById<TextView>(R.id.btnCancel).setOnClickListener {
//                            dialog.dismiss()
//                        }
//
//                        dialogView.findViewById<TextView>(R.id.btnDeleteConfirm).setOnClickListener {
//                           listMembers.removeAt()
//                            dialog.dismiss()
//                            val intent = Intent(this, AdminMainActivity::class.java)
//                            intent.putExtra("fragmentToLoad", "members")
//                            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
//                            startActivity(intent)
//                            finish()
//                        }
//
//                        dialog.show()
//                    }

//                    btnEmail.setOnClickListener {
//                        if (member.isPaid) {
//                            Toast.makeText(this, "The member is subscribed.", Toast.LENGTH_SHORT).show()
//                        } else {
//                            val subject = "Your RiseZone Subscription Has Expired"
//                            val body = "Hello ${member.fullName},\n\nWe hope you're doing well.\n\nThis is a reminder that your subscription to RiseZone has expired.\n\nTo continue benefiting from our services, please renew your subscription at your earliest convenience.\n\nThank you for being part of RiseZone!"
//
//                            val emailIntent = Intent(Intent.ACTION_SEND).apply {
//                                type = "message/rfc822"
//                                putExtra(Intent.EXTRA_EMAIL, arrayOf(member.email))
//                                putExtra(Intent.EXTRA_SUBJECT, subject)
//                                putExtra(Intent.EXTRA_TEXT, body)
//                            }
//
//                            if (emailIntent.resolveActivity(packageManager) != null) {
//                                startActivity(Intent.createChooser(emailIntent, "Send email using"))
//                            } else {
//                                Toast.makeText(this, "No email app found.", Toast.LENGTH_SHORT).show()
//                            }
//                        }
//                    }


//                    btnSubscribed.setOnClickListener {
//                        val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_confirm_change, null)
//                        val dialog = AlertDialog.Builder(this)
//                            .setView(dialogView)
//                            .setCancelable(false)
//                            .create()
//
//                        dialogView.findViewById<TextView>(R.id.btnChange).setOnClickListener {
//                            member.isPaid = !member.isPaid
//
//
//                            btnSubscribed.text = if (member.isPaid) getString(R.string.subscribed) else getString(R.string.not_subscribed)
//
//
//                            textSubscriptionStatus.text = if (member.isPaid) "Subscribed" else "Not Subscribed"
//                            textSubscriptionStatus.setTextColor(if (member.isPaid) Color.GREEN else Color.RED)
//
//
//                            dialog.dismiss()
//                        }
//
//                        dialogView.findViewById<TextView>(R.id.btnCancel).setOnClickListener {
//                            dialog.dismiss()
//                        }
//
//                        dialog.show()
//                    }

                }
            })
        }

        btnBack.setOnClickListener {
            onBackPressed()
        }
    }


}
