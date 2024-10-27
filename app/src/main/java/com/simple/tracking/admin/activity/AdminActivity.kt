package com.simple.tracking.admin.activity

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.simple.tracking.R
import com.simple.tracking.admin.adapter.FragmentPagerAdapter

class AdminActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin)

        val tabLayout = findViewById<TabLayout>(R.id.tabLayout)
        val viewPager = findViewById<ViewPager2>(R.id.viewPager)
        val tabTitles = arrayOf("U S E R", "D A S H B O A R D", "P R O F I L E", "D E L I V E R Y", "S H I P P E R")
        val textView = findViewById<TextView>(R.id.textView18)

        viewPager.adapter = FragmentPagerAdapter(this)

        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            when (position) {
                0 -> {
                    tab.text = "User"
                    tab.setIcon(R.drawable.ic_user)
                }
                1 -> {
                    tab.text = "Dashboard"
                    tab.setIcon(R.drawable.ic_dashboard)
                }
                2 -> {
                    tab.text = "Profile"
                    tab.setIcon(R.drawable.ic_profile)
                }
                3 -> {
                    tab.text = "Delivery"
                    tab.setIcon(R.drawable.ic_delivery)
                }
                4 -> {
                    tab.text = "Shipper"
                    tab.setIcon(R.drawable.ic_shipper)
                }
            }
        }.attach()

        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                textView.text = tabTitles[tab.position]
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {}
            override fun onTabReselected(tab: TabLayout.Tab) {}
        })

        val selectedTab = intent.getIntExtra("DEFAULT_TAB", 1)
        viewPager.setCurrentItem(selectedTab, false)
        textView.text = tabTitles[selectedTab]
    }
}
