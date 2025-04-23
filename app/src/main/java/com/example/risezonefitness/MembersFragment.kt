package com.example.risezonefitness

import android.os.Bundle
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.tabs.TabLayout

class MembersFragment(private val members: List<Member>) : Fragment(R.layout.fragment_members) {

    private lateinit var bottomNav: BottomNavigationView
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: MemberAdapter
    private var isBottomNavVisible = true

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as? AdminMainActivity)?.updateToolbarTitle("Members")

        bottomNav = requireActivity().findViewById(R.id.bottomNav)
        recyclerView = view.findViewById(R.id.recyclerViewMembers)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        adapter = MemberAdapter(members, requireContext())
        recyclerView.adapter = adapter

        val tabs = listOf("All", "Subscriber", "Not Subscriber")
        val tabLayout = view.findViewById<TabLayout>(R.id.filterTabs)

        tabs.forEach {
            tabLayout.addTab(tabLayout.newTab().setText(it))
        }

        var lastSelectedTabIndex = 0

        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                val selected = tab.text.toString()
                val currentIndex = tab.position
                val isLeft = currentIndex > lastSelectedTabIndex

                if (!isBottomNavVisible) {
                    bottomNav.animate().translationY(0f).setDuration(150).start()
                    isBottomNavVisible = true
                }

                val filteredList = when (selected) {
                    "All" -> listMembers
                    "Subscriber" -> listMembers.filter { it.isPaid }
                    "Not Subscriber" -> listMembers.filter { !it.isPaid }
                    else -> listMembers
                }

                animateRecyclerSwap(filteredList, isLeft)
                lastSelectedTabIndex = currentIndex
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {}

            override fun onTabReselected(tab: TabLayout.Tab?) {
                if (!isBottomNavVisible) {
                    bottomNav.animate().translationY(0f).setDuration(150).start()
                    isBottomNavVisible = true
                }
            }
        })

        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                if (dy > 0 && isBottomNavVisible) {
                    bottomNav.animate().translationY(bottomNav.height.toFloat()).setDuration(200).start()
                    isBottomNavVisible = false
                } else if (dy < 0 && !isBottomNavVisible) {
                    bottomNav.animate().translationY(0f).setDuration(150).start()
                    isBottomNavVisible = true
                }
            }
        })
    }

    private fun updateAdapter(members: List<Member>) {
        adapter = MemberAdapter(members, requireContext())
        recyclerView.adapter = adapter
    }

    private fun animateRecyclerSwap(newList: List<Member>, isLeft: Boolean) {
        val outAnim = AnimationUtils.loadAnimation(
            requireContext(),
            if (isLeft) R.anim.slide_out_right else R.anim.slide_out_left
        )
        recyclerView.startAnimation(outAnim)

        outAnim.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation?) {}

            override fun onAnimationEnd(animation: Animation?) {
                updateAdapter(newList)

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
