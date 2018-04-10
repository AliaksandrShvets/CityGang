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
public class Auto {
    private int speedX, speedY;
    private ImageView auto;
    private int[] left, right, up, down;
    private boolean isTruck;
    private boolean isRun;

    public Auto(int speedX, int speedY, ImageView auto, boolean isTruck) {
        this.speedX = speedX;
        this.speedY = speedY;
        this.auto = auto;
        this.isTruck = isTruck;
        isRun = true;
        left = new int[]{R.drawable.anim_rl_car1, R.drawable.anim_rl_car2, R.drawable.anim_rl_car3, R.drawable.anim_rl_car4};
        right = new int[]{R.drawable.anim_lr_car1, R.drawable.anim_lr_car2, R.drawable.anim_lr_car3};
        up = new int[]{R.drawable.anim_du_car2, R.drawable.anim_du_car1, R.drawable.anim_du_car3};
        down = new int[]{R.drawable.anim_ud_car1, R.drawable.anim_ud_car2, R.drawable.anim_ud_car3};
        if (isTruck) {
            if (speedX > 0) auto.setImageResource(R.drawable.anim_lr_truck);
            else if (speedX < 0) auto.setImageResource(R.drawable.anim_rl_truck);
            else if (speedY > 0) auto.setImageResource(R.drawable.anim_ud_track);
            else if (speedY < 0) auto.setImageResource(R.drawable.anim_du_truck);
        } else
            resetAuto();
    }

    public int getSpeedX() {
        return speedX;
    }

    public int getSpeedY() {
        return speedY;
    }

    public boolean isRun() {
        return isRun;
    }

    public void setRun(final boolean isRun, float length) {
        this.isRun = isRun;
        if (!isRun&auto.isEnabled()) {
            auto.setEnabled(false);
            auto.setX(auto.getX() + length * speedX);
            TranslateAnimation translateAnimation = new TranslateAnimation(-length * speedX, 0, 0, 0);
            translateAnimation.setDuration(840);
            translateAnimation.setInterpolator(new DecelerateInterpolator());
            auto.startAnimation(translateAnimation);
            translateAnimation.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    auto.setEnabled(true);
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
        }
    }

    public View getView() {
        return auto;
    }

    public void resetAuto() {
        if (!isTruck) {
            if (speedX > 0) auto.setImageResource(right[new Random().nextInt(right.length)]);
            else if (speedX < 0) auto.setImageResource(left[new Random().nextInt(left.length)]);
            else if (speedY > 0) auto.setImageResource(down[new Random().nextInt(down.length)]);
            else if (speedY < 0) auto.setImageResource(up[new Random().nextInt(up.length)]);
        }
    }
}