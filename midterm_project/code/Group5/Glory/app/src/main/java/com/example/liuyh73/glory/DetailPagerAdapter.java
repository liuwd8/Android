package com.example.liuyh73.glory;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

public class DetailPagerAdapter extends FragmentPagerAdapter {
    private List<Skin> list;
    public DetailPagerAdapter(FragmentManager fm, List<Skin> _list) {
        super(fm);
        list = _list;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return list.get(position).getName();
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Fragment getItem(int i) {
        return SuperAwesomeCardFragment.newInstance(list.get(i).getImg_url(), list.get(i).getAvatar_url(), i);
    }
}
