package com.woodcock.citygang.classes.animation;

import android.view.animation.RotateAnimation;

/**
 * Created by user on 18.09.2016.
 */
public class RotateAnimationCogwheel extends RotateAnimation {
    public RotateAnimationCogwheel(float fromDegrees, float toDegrees, float pivotX, float pivotY, long duration, int repeatMode, int repeatCount) {
        super(fromDegrees, toDegrees, pivotX, pivotY);
        setDuration(duration);
        setRepeatMode(repeatMode);
        setRepeatCount(repeatCount);
    }
}
