package com.woodcock.citygang.classes;

import android.view.View;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;

import com.woodcock.citygang.R;

import java.util.Random;

/**
 * Created by Alex on 31.03.2016.
 */
public class Business {
    private boolean isEnabled;
    private int stars;

    public Business(boolean isEnabled, int stars) {
        this.isEnabled = isEnabled;
        this.stars = stars;
    }

    public boolean isEnabled() {
        return isEnabled;
    }

    public int getStars() {
        return stars;
    }

    public void setEnabled(boolean enabled) {
        isEnabled = enabled;
    }
}