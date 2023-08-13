package com.softperl.urdunovelscollections.AdapterUtil;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.softperl.urdunovelscollections.ObjectUtil.PagerObject;

import java.util.ArrayList;

public class HomePagerAdapter extends FragmentStatePagerAdapter {
    private ArrayList<PagerObject> fragmentArrayList = new ArrayList<>();

    public HomePagerAdapter(FragmentManager fm, ArrayList<PagerObject> fragmentArrayList) {
        super(fm);
        this.fragmentArrayList = fragmentArrayList;
    }


    @Override
    public int getItemPosition(Object object) {
        return super.getItemPosition(object);
    }

    @Override
    public Fragment getItem(int position) {
        return fragmentArrayList.get(position).getFragment();
    }

    @Override
    public int getCount() {
        return fragmentArrayList.size();
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return fragmentArrayList.get(position).getTitle();
    }
}
