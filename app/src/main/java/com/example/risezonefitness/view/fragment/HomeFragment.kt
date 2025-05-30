package com.example.risezonefitness.view.fragment

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.risezonefitness.R
import com.example.risezonefitness.model.LogData
import com.example.risezonefitness.view.activity.AdminMainActivity
import com.example.risezonefitness.view.adapter.GymAttendanceLogAdapter
import com.example.risezonefitness.viewmodel.HomeViewModel

class HomeFragment : Fragment(R.layout.fragment_home) {

    private val homeViewModel: HomeViewModel by viewModels()
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout
    private lateinit var attendanceAdapter: GymAttendanceLogAdapter
    private lateinit var welcomeTextView: TextView
    private lateinit var totalMembersTextView: TextView
    private lateinit var membersInGymTextView: TextView
    private lateinit var nonSubscribedTextView: TextView
    private lateinit var sharedPref: android.content.SharedPreferences
    private var username: String? = null


    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (activity as? AdminMainActivity)?.updateToolbarTitle("Home")

        sharedPref = requireContext().getSharedPreferences("user_settings", Context.MODE_PRIVATE)
        username = sharedPref.getString("username", "User")

        welcomeTextView = view.findViewById<TextView>(R.id.welcomeTextView)
        totalMembersTextView = view.findViewById<TextView>(R.id.totalMembersTextView)
        membersInGymTextView = view.findViewById<TextView>(R.id.membersInGymTextView)
        nonSubscribedTextView = view.findViewById<TextView>(R.id.nonSubscribedTextView)
        val attendanceRecyclerView = view.findViewById<RecyclerView>(R.id.attendanceLogsRecyclerView)
        swipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout)

        attendanceAdapter = GymAttendanceLogAdapter(emptyList())
        attendanceRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        attendanceRecyclerView.adapter = attendanceAdapter

        username?.let {
            homeViewModel.getFullName(it).observe(viewLifecycleOwner) { fullName ->
                welcomeTextView.text = "Welcome, $fullName"
            }
        }

        homeViewModel.totalMembers.observe(viewLifecycleOwner) { count ->
            totalMembersTextView.text = "Total Members: $count"
        }

        homeViewModel.membersInGym.observe(viewLifecycleOwner) { count ->
            membersInGymTextView.text = "Members in Gym: $count"
        }

        homeViewModel.nonSubscribedMembers.observe(viewLifecycleOwner) { count ->
            nonSubscribedTextView.text = "Non-Subscribed: $count"
        }

        homeViewModel.gymAttendanceLogs.observe(viewLifecycleOwner) { logs ->
            attendanceAdapter.updateLogs(logs)
            swipeRefreshLayout.isRefreshing = false
        }

        swipeRefreshLayout.setOnRefreshListener {
            reloadData()
        }
    }

    private fun reloadData() {

        username?.let {
            homeViewModel.getFullName(it).observe(viewLifecycleOwner) { fullName ->
                welcomeTextView.text = "Welcome, $fullName"
            }
        }

        homeViewModel.totalMembers.observe(viewLifecycleOwner) { count ->
            totalMembersTextView.text = "Total Members: $count"
        }

        homeViewModel.membersInGym.observe(viewLifecycleOwner) { count ->
            membersInGymTextView.text = "Members in Gym: $count"
        }

        homeViewModel.nonSubscribedMembers.observe(viewLifecycleOwner) { count ->
            nonSubscribedTextView.text = "Non-Subscribed: $count"
        }
        homeViewModel.gymAttendanceLogs.observe(viewLifecycleOwner) { logs ->
            attendanceAdapter.updateLogs(logs)
            swipeRefreshLayout.isRefreshing = false
        }
    }
}
