package com.example.risezonefitness

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
        }

        btnBack.setOnClickListener {
            onBackPressed()
        }
    }

}
