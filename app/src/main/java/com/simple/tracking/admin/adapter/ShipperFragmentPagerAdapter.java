package com.simple.tracking.admin.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.simple.tracking.admin.fragment.DashboardFragment;
import com.simple.tracking.admin.fragment.DeliveryFragment;
import com.simple.tracking.admin.fragment.ProfileFragment;
import com.simple.tracking.admin.fragment.ShipperFragment;
import com.simple.tracking.admin.fragment.UserFragment;

public class ShipperFragmentPagerAdapter extends FragmentStateAdapter {

    public ShipperFragmentPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @Override
    public int getItemCount() {
        return 3;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return new ProfileFragment();
            case 1:
                return new DashboardFragment();
            case 2:
                return new DeliveryFragment();
            default:
                return new DashboardFragment();
        }
    }
}
