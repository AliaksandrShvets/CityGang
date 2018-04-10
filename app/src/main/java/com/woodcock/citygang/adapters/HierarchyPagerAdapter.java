package com.woodcock.citygang.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.woodcock.citygang.fragments.HierarchyItem;

import java.util.ArrayList;

/**
 * Created by Alex on 28.02.2016.
 */
public class HierarchyPagerAdapter extends FragmentPagerAdapter {
    ArrayList<HierarchyItem> items;

    public HierarchyPagerAdapter(FragmentManager fragmentManager, ArrayList<HierarchyItem> hierarchyItems) {
        super(fragmentManager);
        items = hierarchyItems;
    }

    @Override
    public Fragment getItem(int position) {
        return items.get(position);
    }

    @Override
    public float getPageWidth(int position) {
        return (1 / 3f);
    }

    @Override
    public int getCount() {
        return items.size();
    }
}
