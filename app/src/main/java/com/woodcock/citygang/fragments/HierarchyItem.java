package com.woodcock.citygang.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.woodcock.citygang.R;
import com.woodcock.citygang.classes.Constants;

public class HierarchyItem extends Fragment {
    static Activity activityParent;
    int page, count,
            stars[] = {R.drawable.stars0,R.drawable.stars1,R.drawable.stars2,R.drawable.stars3,R.drawable.stars4,R.drawable.stars5},
            business[] = {R.drawable.business0_element1, R.drawable.business0_element0, R.drawable.business0_element2, R.drawable.business1_element1, R.drawable.business1_element2, R.drawable.business1_element3, R.drawable.business2_element0, R.drawable.business2_element1, R.drawable.business2_element2, R.drawable.business3_element0},
            businessDisable[] = {R.drawable.business0_element1_disable, R.drawable.business0_element0_disable, R.drawable.business0_element2_disable, R.drawable.business1_element1_disable, R.drawable.business1_element2_disable, R.drawable.business1_element3_disable, R.drawable.business2_element0_disable, R.drawable.business2_element1_disable, R.drawable.business2_element2_disable, R.drawable.business3_element0_disable};

    View view;

    public static HierarchyItem newInstance(Activity activity, int page) {
        activityParent = activity;
        HierarchyItem pageFragment = new HierarchyItem();
        Bundle arguments = new Bundle();
        arguments.putInt(Constants.ARGUMENT_PAGE_NUMBER, page);
        pageFragment.setArguments(arguments);
        return pageFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.item_hierarchy, null);
        page = getArguments().getInt(Constants.ARGUMENT_PAGE_NUMBER);
        count = getActivity().getSharedPreferences(Constants.SP, getActivity().MODE_PRIVATE).getInt(Constants.SP_COUNT_OPENED_BUSINESSES, 1);
        if (page == 0 | page == business.length+1) {
            view.setVisibility(View.GONE);
        } else {
            update(count >= page);
        }
        return view;
    }

    public void update(boolean isEnabled) {
        if (view != null) {
            ((ImageView) view.findViewById(R.id.image)).setImageResource(isEnabled ? business[page - 1] : businessDisable[page - 1]);
            ((ImageView) view.findViewById(R.id.stars)).setImageResource(stars[getActivity().getSharedPreferences(Constants.SP, getActivity().MODE_PRIVATE).getInt(Constants.SP_COUNT_STARS + page, 0)]);
        }
    }

    public void setSelect(boolean isSelected) {
        view.setSelected(isSelected);
        view.setScaleX(isSelected ? 1.2f : 1);
        view.setScaleY(isSelected ? 1.2f : 1);
    }
}