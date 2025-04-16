package com.example.risezonefitness




import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment

class HomeFragment : Fragment(R.layout.fragment_home) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        (activity as? AdminMainActivity)?.updateToolbarTitle("Home")

    }

}
