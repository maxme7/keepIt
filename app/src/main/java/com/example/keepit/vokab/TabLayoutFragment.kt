package com.example.keepit.vokab

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.example.keepit.R
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class TabLayoutFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        val fragm = inflater.inflate(R.layout.fragment_tab_layout, container, false)

        val tabLayout = fragm.findViewById<TabLayout>(R.id.tabLayout)
        val viewPager2 = fragm.findViewById<ViewPager2>(R.id.pager)

        val fragmentAdapter = activity?.let { FragmentAdapter(it.supportFragmentManager, lifecycle) }
        viewPager2?.adapter = fragmentAdapter

        (activity as AppCompatActivity).supportActionBar?.hide()


        //TODO needs mediator so navigation between fragments works
        //TODO title has to be set here? otherwise blank
        if (tabLayout != null && viewPager2 != null) {
            TabLayoutMediator(tabLayout, viewPager2) { tab, position ->
                tab.icon = when (position) {
                    0 -> resources.getDrawable(R.drawable.ic_list_icon)
                    1 -> resources.getDrawable(R.drawable.ic_files_icon)
                    else -> resources.getDrawable(R.drawable.ic_baseline_notifications_active_24)
                }
            }.attach()
        }

        return fragm
    }

    override fun onPause() {
        super.onPause()
        (activity as AppCompatActivity).supportActionBar?.show()
    }
}