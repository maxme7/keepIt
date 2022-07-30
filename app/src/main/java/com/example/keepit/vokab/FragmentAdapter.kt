package com.example.keepit.vokab

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.keepit.fragments.SettingsFragment
import com.example.keepit.fragments.testFragment

class FragmentAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle) : FragmentStateAdapter(fragmentManager, lifecycle) {

    override fun createFragment(position: Int): Fragment {
        val fragment: Fragment

        if (position == 0) {
            fragment = testFragment()
        } else {
            fragment = SettingsFragment()
        }

        return fragment
    }

    override fun getItemCount(): Int {
        return 3
    }

}

// https://www.youtube.com/watch?v=VxsYMeTMClU