package com.simple.tracking.admin.activity;

import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.simple.tracking.R;
import com.simple.tracking.admin.adapter.FragmentPagerAdapter;

public class AdminActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        TabLayout tabLayout = findViewById(R.id.tabLayout);
        ViewPager2 viewPager = findViewById(R.id.viewPager);
        TextView textView = findViewById(R.id.textView18);

        String[] tabTitles = new String[]{"U S E R", "D A S H B O A R D", "P R O F I L E", "D E L I V E R Y", "S H I P P E R"};

        viewPager.setAdapter(new FragmentPagerAdapter(this));

        new TabLayoutMediator(tabLayout, viewPager, (tab, position) -> {
            switch (position) {
                case 0:
                    tab.setText("User");
                    tab.setIcon(R.drawable.ic_user);
                    break;
                case 1:
                    tab.setText("Dashboard");
                    tab.setIcon(R.drawable.ic_dashboard);
                    break;
                case 2:
                    tab.setText("Profile");
                    tab.setIcon(R.drawable.ic_profile);
                    break;
                case 3:
                    tab.setText("Delivery");
                    tab.setIcon(R.drawable.ic_delivery);
                    break;
                case 4:
                    tab.setText("Shipper");
                    tab.setIcon(R.drawable.ic_shipper);
                    break;
            }
        }).attach();

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(@NonNull TabLayout.Tab tab) {
                textView.setText(tabTitles[tab.getPosition()]);
            }

            @Override
            public void onTabUnselected(@NonNull TabLayout.Tab tab) {}

            @Override
            public void onTabReselected(@NonNull TabLayout.Tab tab) {}
        });

        int selectedTab = getIntent().getIntExtra("DEFAULT_TAB", 1);
        viewPager.setCurrentItem(selectedTab, false);
        textView.setText(tabTitles[selectedTab]);
    }
}
