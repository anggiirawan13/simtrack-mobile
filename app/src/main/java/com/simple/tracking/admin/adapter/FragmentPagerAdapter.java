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

public class FragmentPagerAdapter extends FragmentStateAdapter {

    public FragmentPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @Override
    public int getItemCount() {
        return 5;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return new UserFragment();
            case 1:
                return new DashboardFragment();
            case 2:
                return new ProfileFragment();
            case 3:
                return new DeliveryFragment();
            case 4:
                return new ShipperFragment();
            default:
                return new DashboardFragment();
        }
    }
}
