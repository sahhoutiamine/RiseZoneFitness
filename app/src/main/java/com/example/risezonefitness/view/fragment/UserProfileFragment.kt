package com.example.risezonefitness.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.Fragment
import com.example.risezonefitness.R
import com.example.risezonefitness.data.listMembers
import com.example.risezonefitness.view.activity.UserMainActivity
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class UserProfileFragment : Fragment() {

    private lateinit var textFullName: TextView
    private lateinit var textAge: TextView
    private lateinit var textGender: TextView
    private lateinit var textStatus: TextView
    private lateinit var textPhoneNumber: TextView
    private lateinit var textEmail: TextView
    private lateinit var textCin: TextView
    private lateinit var textWeekStreak: TextView
    private lateinit var textLastSession: TextView
    private lateinit var textRegistrationDate: TextView
    private lateinit var textSubscriptionStatus: TextView
    private lateinit var btnBack: ImageButton
    private lateinit var imgAvatar: ImageView
    private var username: String? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_profile, container, false)

        (activity as? UserMainActivity)?.updateToolbarTitle("Profile")

        textFullName = view.findViewById(R.id.textFullName)
        textAge = view.findViewById(R.id.textAge)
        textGender = view.findViewById(R.id.textGender)
        textStatus = view.findViewById(R.id.textInGym)
        textPhoneNumber = view.findViewById(R.id.text_number)
        textEmail = view.findViewById(R.id.text_email)
        textCin = view.findViewById(R.id.text_cin)
        textWeekStreak = view.findViewById(R.id.text_week_streak)
        textLastSession = view.findViewById(R.id.text_last_session)
        textRegistrationDate = view.findViewById(R.id.text_reg_date)
        textSubscriptionStatus = view.findViewById(R.id.textSubscriptionStatus)
        btnBack = view.findViewById(R.id.btnBack)
        imgAvatar = view.findViewById(R.id.imgMemberAvatar)

        btnBack.setOnClickListener {
            activity?.onBackPressed()
        }

        username = arguments?.getString("username")

        val member = listMembers.find { it.username == username }

        member?.let {
            textFullName.text = it.fullName
            textAge.text = it.age.toString()
            textGender.text = it.gender
            textPhoneNumber.text = it.phoneNumber
            textEmail.text = it.email
            textCin.text = it.cin
            textWeekStreak.text = it.attendanceThisWeek.toString()

            val lastSessionFormatted = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                .format(Date(it.lastAttendanceReset))
            textLastSession.text = lastSessionFormatted

            val regDateFormatted = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                .format(Date(it.registrationDate))
            textRegistrationDate.text = regDateFormatted

            textSubscriptionStatus.text = if (it.isPaid) "Subscribed" else "Not Subscribed"
            textStatus.text = if (it.isInGym) "In Gym" else "Not In Gym"

            it.imageResource?.let { bitmap ->
                imgAvatar.setImageBitmap(bitmap)
            }
        }

        val scrollView = view.findViewById<NestedScrollView>(R.id.scrollView)
        var lastScrollY = 0
        scrollView.setOnScrollChangeListener { _, _, scrollY, _, oldScrollY ->
            val userActivity = activity as? UserMainActivity
            val bottomNav = userActivity?.findViewById<View>(R.id.bottomNav)

            if (scrollY > oldScrollY + 10) {
                bottomNav?.animate()?.translationY(bottomNav.height.toFloat())?.setDuration(200)?.start()
            } else if (scrollY < oldScrollY - 10) {
                bottomNav?.animate()?.translationY(0f)?.setDuration(100)?.start()
            }

            lastScrollY = scrollY
        }

        return view
    }
}