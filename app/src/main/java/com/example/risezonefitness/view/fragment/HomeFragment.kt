package com.example.risezonefitness.view.fragment

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.example.risezonefitness.viewmodel.HomeViewModel
import com.example.risezonefitness.R
import com.example.risezonefitness.view.activity.AdminMainActivity

class HomeFragment : Fragment(R.layout.fragment_home) {

    private val homeViewModel: HomeViewModel by viewModels()

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (activity as? AdminMainActivity)?.updateToolbarTitle("Home")

        val sharedPref = requireContext().getSharedPreferences("user_settings", Context.MODE_PRIVATE)
        val username = sharedPref.getString("username", "User")

        val welcomeTextView = view.findViewById<TextView>(R.id.welcomeTextView)
        val totalMembersTextView = view.findViewById<TextView>(R.id.totalMembersTextView)
        val membersInGymTextView = view.findViewById<TextView>(R.id.membersInGymTextView)
        val nonSubscribedTextView = view.findViewById<TextView>(R.id.nonSubscribedTextView)

        username?.let {
            homeViewModel.getFullName(it).observe(viewLifecycleOwner, Observer { fullName ->
                welcomeTextView.text = "Welcome, $fullName"
            })
        }

        homeViewModel.totalMembers.observe(viewLifecycleOwner, Observer { count ->
            totalMembersTextView.text = "Total Members: $count"
        })

        homeViewModel.membersInGym.observe(viewLifecycleOwner, Observer { count ->
            membersInGymTextView.text = "Members in Gym: $count"
        })

        homeViewModel.nonSubscribedMembers.observe(viewLifecycleOwner, Observer { count ->
            nonSubscribedTextView.text = "Non-Subscribed: $count"
        })
    }
}