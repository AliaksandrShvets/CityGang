package com.woodcock.citygang.fragments;

import android.app.Fragment;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import com.woodcock.citygang.MainUI;
import com.woodcock.citygang.R;
import com.woodcock.citygang.classes.Constants;

/**
 * Created by Alex on 06.04.2016.
 */
public class PersonageInfo extends Fragment {
    static String personage;
    View view;
    ImageView imageView;
    int width;
    boolean isRightSide=false;

    public static PersonageInfo newInstance(String type) {
        personage = type;
        return new PersonageInfo();
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.personage_info, null);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });

        DisplayMetrics display = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(display);
        width = display.widthPixels;
        ((TextView) view.findViewById(R.id.personage_description)).setTextSize(TypedValue.COMPLEX_UNIT_PX, width / 21);
        ((TextView) view.findViewById(R.id.personage_description)).setTypeface(Typeface.createFromAsset(getActivity().getAssets(), "9364.ttf"));
        imageView = (ImageView) view.findViewById(R.id.personage_image);

        animationIn();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        ((MainUI) getActivity()).getAnalytics().Send(Constants.SCREEN_PERSONAGE);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        ((MainUI) getActivity()).setPersonageInfoOpenedFalse();
    }

    public void setNewInstance(String type) {
        if (!type.equals(personage)) {
            int prev, next;
            prev = personage.equals("As") ? 1 : personage.equals("Ng") ? 2 : personage.equals("Mx") ? 3 : personage.equals("Sd") ? 4 : 5;
            next = type.equals("As") ? 1 : type.equals("Ng") ? 2 : type.equals("Mx") ? 3 : type.equals("Sd") ? 4 : 5;
            isRightSide = prev > next ? true : false;
            if (!personage.equals(type))
                animationOut();
            personage = type;
        }
    }

    public void animationOut() {
        fillImage(personage);
        TranslateAnimation translate = new TranslateAnimation(0, isRightSide?width:-width, 0, 0);
        translate.setDuration(400);
        translate.setInterpolator(new AccelerateInterpolator());
        translate.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                animationIn();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        view.findViewById(R.id.personage_image).startAnimation(translate);
        translate = new TranslateAnimation(0, width, 0, 0);
        translate.setDuration(400);
        translate.setInterpolator(new AccelerateInterpolator());

        AlphaAnimation alpha = new AlphaAnimation(1, 0);
        alpha.setDuration(400);
        view.findViewById(R.id.panel_name).startAnimation(alpha);
        view.findViewById(R.id.personage_description).startAnimation(alpha);
    }

    public void animationIn() {
        fillImage(personage);
        TranslateAnimation translate = new TranslateAnimation(isRightSide?-width:width, 0, 0, 0);
        translate.setDuration(400);
        translate.setInterpolator(new DecelerateInterpolator());
        imageView.startAnimation(translate);
        translate = new TranslateAnimation(-width, 0, 0, 0);
        translate.setDuration(400);
        translate.setInterpolator(new AccelerateInterpolator());

        AlphaAnimation alpha = new AlphaAnimation(0, 1);
        alpha.setDuration(400);
        view.findViewById(R.id.panel_name).startAnimation(alpha);
        view.findViewById(R.id.personage_description).startAnimation(alpha);

        alpha = new AlphaAnimation(1, 0);
        alpha.setDuration(400);
        view.findViewById(R.id.panel_light).startAnimation(alpha);
        alpha.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                AlphaAnimation alpha = new AlphaAnimation(0, 1);
                alpha.setDuration(400);
                view.findViewById(R.id.panel_light).startAnimation(alpha);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    public void fillImage(String type) {
        switch (type) {
            case "As":
                ((ImageView) view.findViewById(R.id.personage_image)).setImageResource(R.drawable.personage_assassin);
                ((ImageView) view.findViewById(R.id.panel_name)).setImageResource(R.drawable.personage_info_name_assassin);
                ((TextView) view.findViewById(R.id.personage_description)).setText(getResources().getString(R.string.info_assassin));
                break;
            case "Ng":
                ((ImageView) view.findViewById(R.id.personage_image)).setImageResource(R.drawable.personage_nigger);
                ((ImageView) view.findViewById(R.id.panel_name)).setImageResource(R.drawable.personage_info_name_nigger);
                ((TextView) view.findViewById(R.id.personage_description)).setText(getResources().getString(R.string.info_gangster));
                break;
            case "Mx":
                ((ImageView) view.findViewById(R.id.personage_image)).setImageResource(R.drawable.personage_mexican);
                ((ImageView) view.findViewById(R.id.panel_name)).setImageResource(R.drawable.personage_info_name_mexican);
                ((TextView) view.findViewById(R.id.personage_description)).setText(getResources().getString(R.string.info_mexican));
                break;
            case "Sd":
                ((ImageView) view.findViewById(R.id.personage_image)).setImageResource(R.drawable.personage_soldier);
                ((ImageView) view.findViewById(R.id.panel_name)).setImageResource(R.drawable.personage_info_name_soldier);
                ((TextView) view.findViewById(R.id.personage_description)).setText(getResources().getString(R.string.info_soldier));
                break;
            case "Iv":
                ((ImageView) view.findViewById(R.id.personage_image)).setImageResource(R.drawable.personage_invalid);
                ((ImageView) view.findViewById(R.id.panel_name)).setImageResource(R.drawable.personage_info_name_invalid);
                ((TextView) view.findViewById(R.id.personage_description)).setText(getResources().getString(R.string.info_invalid));
                break;
        }
    }
}