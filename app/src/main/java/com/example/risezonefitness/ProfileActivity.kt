package com.example.risezonefitness

import android.graphics.Bitmap
import android.graphics.Color
import android.os.Bundle
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
    private lateinit var btnBack: ImageButton
    private lateinit var imgAvatar: ImageView
    private lateinit var textSubscriptionStatus: TextView

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
        btnBack = findViewById(R.id.btnBack)
        imgAvatar = findViewById(R.id.imgMemberAvatar)
        textSubscriptionStatus = findViewById(R.id.textSubscriptionStatus)

        val fullName = intent.getStringExtra("fullName")
        val age = intent.getIntExtra("age", 0)
        val gender = intent.getStringExtra("gender")
        val phoneNumber = intent.getStringExtra("phoneNumber")
        val email = intent.getStringExtra("email")
        val cin = intent.getStringExtra("cin")
        val username = intent.getStringExtra("username")
        val password = intent.getStringExtra("password")
        val isPaid = intent.getBooleanExtra("isPaid", true)
        val isInGym = intent.getBooleanExtra("isInGym", true)
        val registrationDate = intent.getLongExtra("registrationDate", System.currentTimeMillis())

        textFullName.text = fullName
        textAge.text = age.toString()
        textGender.text = gender
        textStatus.text = if (isInGym) "In Gym" else "Not In Gym"
        textPhoneNumber.text = phoneNumber
        textEmail.text = email
        textCin.text = cin
        textUsername.text = username
        textPassword.text = password
        textRegistrationDate.text = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date(registrationDate))

        textSubscriptionStatus.text = if (isPaid) "Subscribed" else "Not Subscribed"
        textSubscriptionStatus.setTextColor(if (isPaid) Color.GREEN else Color.RED)

        val avatarBitmap = intent.getParcelableExtra<Bitmap>("imageResource")
        if (avatarBitmap != null) {
            imgAvatar.setImageBitmap(avatarBitmap)
        } else {
            imgAvatar.setImageResource(R.drawable.ic_person)
        }

        btnBack.setOnClickListener {
            onBackPressed()
        }
    }
}
