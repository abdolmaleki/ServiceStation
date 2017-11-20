package com.technotapp.servicestation.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.technotapp.servicestation.fragment.MainGridFragment;

import java.util.List;


public class MainMenuPageAdapter extends FragmentStatePagerAdapter {

    private List<MainGridFragment> fragments;

    public MainMenuPageAdapter(FragmentManager fm, List<MainGridFragment> fragments) {
        super(fm);
        this.fragments = fragments;
    }

    @Override
    public Fragment getItem(int pos) {
        return this.fragments.get(pos);
    }

    @Override
    public int getCount() {
        return this.fragments.size();
    }
}
