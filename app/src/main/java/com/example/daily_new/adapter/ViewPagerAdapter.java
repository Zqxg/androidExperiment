package com.example.daily_new.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.daily_new.view.CustomFragment;
import com.example.daily_new.view.GuessNumberFragment;
import com.example.daily_new.view.HomeFragment;
import com.example.daily_new.view.IntegrationFragment;
import com.example.daily_new.view.ProfileFragment;
import com.example.daily_new.view.ScheduleFragment;

public class ViewPagerAdapter extends FragmentStateAdapter {
    public ViewPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return new HomeFragment();
            case 1:
                return new ScheduleFragment();
            case 2:
                return new IntegrationFragment();
            case 3:
                return new GuessNumberFragment();
            default:
                return new HomeFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 4; // 4 个页面
    }
}

