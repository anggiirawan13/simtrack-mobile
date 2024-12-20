package com.simple.tracking.admin.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.OnBackPressedCallback;
import androidx.activity.OnBackPressedDispatcher;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.simple.tracking.LocationChecker;
import com.simple.tracking.LogoutActivity;
import com.simple.tracking.R;
import com.simple.tracking.admin.adapter.FragmentPagerAdapter;
import com.simple.tracking.admin.adapter.ShipperFragmentPagerAdapter;

public class ShipperActivity extends AppCompatActivity {

    private ImageView btnLogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        if (!LocationChecker.isLocationEnabled(this)) {
            LocationChecker.showLocationDisabledDialog(this);
        }

        OnBackPressedDispatcher onBackPressedDispatcher = getOnBackPressedDispatcher();
        onBackPressedDispatcher.addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                Intent intent = new Intent(ShipperActivity.this, LogoutActivity.class);
                startActivity(intent);
            }
        });

        ImageView btnLogout = findViewById(R.id.btn_logout);
        btnLogout.setOnClickListener(v -> {
            Intent intent = new Intent(ShipperActivity.this, LogoutActivity.class);
            startActivity(intent);
        });

        TabLayout tabLayout = findViewById(R.id.tabLayout);
        ViewPager2 viewPager = findViewById(R.id.viewPager);
        TextView textView = findViewById(R.id.textView18);

        String[] tabTitles = new String[]{"P R O F I L E", "D A S H B O A R D", "D E L I V E R Y"};

        viewPager.setAdapter(new ShipperFragmentPagerAdapter(this));
        viewPager.setOffscreenPageLimit(1);

        new TabLayoutMediator(tabLayout, viewPager, (tab, position) -> {
            switch (position) {
                case 0:
                    tab.setText("Profile");
                    tab.setIcon(R.drawable.ic_profile);
                    break;
                case 1:
                    tab.setText("Dashboard");
                    tab.setIcon(R.drawable.ic_dashboard);
                    break;
                case 2:
                    tab.setText("Delivery");
                    tab.setIcon(R.drawable.ic_delivery);
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
        viewPager.setUserInputEnabled(false);
        textView.setText(tabTitles[selectedTab]);
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (!LocationChecker.isLocationEnabled(this)) {
            LocationChecker.showLocationDisabledDialog(this);
        }
    }
}
