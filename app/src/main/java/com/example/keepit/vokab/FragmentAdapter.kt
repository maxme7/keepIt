package com.example.keepit.vokab

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.keepit.fragments.*

class FragmentAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle) : FragmentStateAdapter(fragmentManager, lifecycle) {


    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> CollectionSortingFragment()
            1 -> CollectionOverviewFragment()
            else -> FlashCardsManagmentFragment()
        }
    }

    override fun getItemCount(): Int {
        return 3
    }

}

// https://www.youtube.com/watch?v=VxsYMeTMClU