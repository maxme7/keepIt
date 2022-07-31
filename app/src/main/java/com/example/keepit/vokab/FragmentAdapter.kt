package com.example.keepit.vokab

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.keepit.fragments.CategorySortingFragment
import com.example.keepit.fragments.SettingsFragment
import com.example.keepit.fragments.testFragment

class FragmentAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle) : FragmentStateAdapter(fragmentManager, lifecycle) {


    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> CategorySortingFragment()
            1 -> testFragment()
            else -> SettingsFragment()
        }
    }

    override fun getItemCount(): Int {
        return 3
    }

}

// https://www.youtube.com/watch?v=VxsYMeTMClU