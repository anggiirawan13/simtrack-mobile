package com.simple.tracking

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class AdminFragmentPagerAdapter(fragmentActivity: FragmentActivity) : FragmentStateAdapter(fragmentActivity) {

    override fun getItemCount(): Int {
        return 5
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> UserFragment()
            1 -> DashboardFragment()
            2 -> ProfileFragment()
            3 -> DeliveryFragment()
            4 -> ShipperFragment()
            else -> DashboardFragment()
        }
    }
}
