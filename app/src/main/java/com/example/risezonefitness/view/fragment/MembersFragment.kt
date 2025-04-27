package com.example.risezonefitness.view.fragment

import android.os.Bundle
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.risezonefitness.view.adapter.MemberAdapter
import com.example.risezonefitness.viewmodel.MembersViewModel
import com.example.risezonefitness.R
import com.example.risezonefitness.model.Member
import com.example.risezonefitness.view.activity.AdminMainActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.tabs.TabLayout

class MembersFragment : Fragment(R.layout.fragment_members) {

    private lateinit var viewModel: MembersViewModel
    private lateinit var recyclerView: RecyclerView
    private lateinit var bottomNav: BottomNavigationView
    private lateinit var adapter: MemberAdapter
    private var isBottomNavVisible = true

    private var fullList: List<Member> = emptyList()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (activity as? AdminMainActivity)?.updateToolbarTitle("Members")

        bottomNav = requireActivity().findViewById(R.id.bottomNav)
        recyclerView = view.findViewById(R.id.recyclerViewMembers)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        adapter = MemberAdapter(emptyList(), requireContext())
        recyclerView.adapter = adapter

        viewModel = ViewModelProvider(this)[MembersViewModel::class.java]

        setupTabs(view)
        setupScrolling()
        observeMembers()
    }

    private fun observeMembers() {
        lifecycleScope.launchWhenStarted {
            viewModel.members.collect { list ->
                fullList = list
                filterAndShow("All")
            }
        }
    }

    private fun setupTabs(view: View) {
        val tabLayout = view.findViewById<TabLayout>(R.id.filterTabs)
        val tabs = listOf("All", "Subscriber", "Not Subscriber")
        tabs.forEach { tabLayout.addTab(tabLayout.newTab().setText(it)) }

        var lastTab = 0

        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                val selected = tab.text.toString()
                val isLeft = tab.position > lastTab
                filterAndShow(selected, isLeft)
                lastTab = tab.position

                if (!isBottomNavVisible) {
                    bottomNav.animate().translationY(0f).setDuration(150).start()
                    isBottomNavVisible = true
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
                if (!isBottomNavVisible) {
                    bottomNav.animate().translationY(0f).setDuration(150).start()
                    isBottomNavVisible = true
                }
            }
        })

    }

    private fun filterAndShow(selected: String, isLeft: Boolean = true) {
        val filteredList = when (selected) {
            "All" -> fullList
            "Subscriber" -> fullList.filter { it.isPaid }
            "Not Subscriber" -> fullList.filter { !it.isPaid }
            else -> fullList
        }
        animateRecyclerSwap(filteredList, isLeft)
    }

    private fun setupScrolling() {
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(rv: RecyclerView, dx: Int, dy: Int) {
                if (dy > 0 && isBottomNavVisible) {
                    bottomNav.animate().translationY(bottomNav.height.toFloat()).duration = 200
                    isBottomNavVisible = false
                } else if (dy < 0 && !isBottomNavVisible) {
                    bottomNav.animate().translationY(0f).duration = 150
                    isBottomNavVisible = true
                }
            }
        })
    }

    private fun animateRecyclerSwap(newList: List<Member>, isLeft: Boolean) {
        val outAnim = AnimationUtils.loadAnimation(requireContext(),
            if (isLeft) R.anim.slide_out_right else R.anim.slide_out_left
        )

        recyclerView.startAnimation(outAnim)

        outAnim.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation?) {}
            override fun onAnimationEnd(animation: Animation?) {
                adapter = MemberAdapter(newList, requireContext())
                recyclerView.adapter = adapter

                val inAnim = AnimationUtils.loadLayoutAnimation(
                    requireContext(),
                    if (isLeft) R.anim.layout_animation_from_right else R.anim.layout_animation_from_left
                )
                recyclerView.layoutAnimation = inAnim
                recyclerView.scheduleLayoutAnimation()
            }

            override fun onAnimationRepeat(animation: Animation?) {}
        })
    }
}