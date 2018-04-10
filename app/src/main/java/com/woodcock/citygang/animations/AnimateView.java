package com.woodcock.citygang.animations;

import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;

import java.util.Random;

/**
 * Created by Alex on 29.03.2016.
 */
public class AnimateView {
    private ImageView view;
    private TranslateAnimation set;
    int durationStart, cooldownAnim, durationRandom;
    float height;

    public AnimateView(ImageView view, TranslateAnimation set, int durationStart, int cooldownAnim, int durationRandom, float heightField) {
        this.view = view;
        this.set = set;
        this.durationStart = durationStart;
        this.cooldownAnim = cooldownAnim;
        this.durationRandom = durationRandom;
        this.height = heightField;
    }

    public void start() {
        set.setStartOffset(durationStart);
        view.startAnimation(set);
        set.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                if (cooldownAnim >= 0) {
                    set.setStartOffset(cooldownAnim + new Random().nextInt(durationRandom));
                    if (cooldownAnim == 0 & durationRandom > 1)
                        view.setY(new Random().nextInt((int) height-view.getHeight()));
                    view.startAnimation(set);
                } else view.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });
    }

    public void deleteAnimation() {
        view.clearAnimation();
    }
}
