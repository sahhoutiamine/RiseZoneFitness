package com.example.risezonefitness

import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView

class MembersFragment(private val members: List<Member>) : Fragment(R.layout.fragment_members) {

    private lateinit var bottomNav: BottomNavigationView
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: MemberAdapter
    private var isBottomNavVisible = true  // متغير لتتبع حالة ظهور BottomNavigationView

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val buttonAll = view.findViewById<Button>(R.id.btnAll)
        val buttonPaid = view.findViewById<Button>(R.id.btnPaid)
        val buttonUnpaid = view.findViewById<Button>(R.id.btnUnpaid)

        bottomNav = requireActivity().findViewById(R.id.bottomNav)
        recyclerView = view.findViewById(R.id.recyclerViewMembers)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        adapter = MemberAdapter(members)
        recyclerView.adapter = adapter
        val allButtons = listOf(buttonAll, buttonPaid, buttonUnpaid)

        fun selectButton(selectedButton: Button) {
            allButtons.forEach { button ->
                if (button == selectedButton) {
                    button.animate()
                        .alpha(1f)
                        .scaleX(1.05f)
                        .scaleY(1.05f)
                        .setDuration(150)
                        .withEndAction {
                            button.setBackgroundResource(R.drawable.filter_button_selected)
                            button.animate()
                                .scaleX(1f)
                                .scaleY(1f)
                                .setDuration(100)
                                .start()
                        }
                        .start()
                } else {
                    button.setBackgroundResource(R.drawable.filter_button_unselected)
                    button.animate()
                        .alpha(1f)
                        .scaleX(1f)
                        .scaleY(1f)
                        .setDuration(100)
                        .start()
                }
            }
        }

        selectButton(buttonAll)

        buttonAll.setOnClickListener {
            updateAdapter(listMembers)
            selectButton(buttonAll)
            animateRecyclerViewWithSlide()
        }

        buttonPaid.setOnClickListener {
            val filtered = listMembers.filter { it.isPaid }
            updateAdapter(filtered)
            selectButton(buttonPaid)
            animateRecyclerViewWithSlide()
        }

        buttonUnpaid.setOnClickListener {
            val filtered = listMembers.filter { !it.isPaid }
            updateAdapter(filtered)
            selectButton(buttonUnpaid)
            animateRecyclerViewWithSlide()
        }

        // إخفاء الـ BottomNavigationView عند التمرير
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

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (!isBottomNavVisible) {
                    bottomNav.animate()
                        .translationY(0f)
                        .setDuration(150)
                        .start()
                    isBottomNavVisible = true
                } else {
                    requireActivity().onBackPressed()
                }
            }
        })

    }

    private fun updateAdapter(members: List<Member>) {
        adapter = MemberAdapter(members)
        recyclerView.adapter = adapter
    }

    private fun animateRecyclerViewWithSlide() {
        recyclerView.translationY = 100f
        recyclerView.animate().translationY(0f)
            .setDuration(500)
            .start()
    }
}
