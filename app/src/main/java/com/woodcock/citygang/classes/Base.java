package com.woodcock.citygang.classes;

import android.app.Activity;
import android.media.MediaPlayer;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import com.woodcock.citygang.MainUI;
import com.woodcock.citygang.R;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Alex on 16.02.2016.
 */
public class Base {
    private View view;
    private Integer hp;
    float leftHP;
    private boolean isAlly, isDead = false, isUnderAttack[] = {false, false, false}, baseUnderAttack, isSoundPlay = false;
    private MediaPlayer sound;
    private Activity activity;

    public Base(Activity activity, View view, boolean isAlly) {
        this.activity = activity;
        this.view = view;
        this.isAlly = isAlly;
        leftHP = 4000;
        hp = 4000;
    }

    public View getView() {
        return view;
    }

    public boolean isAlly() {
        return isAlly;
    }

    public boolean isDead() {
        return isDead;
    }

    public MediaPlayer getSound() {
        return sound;
    }

    public void soundPause() {
        if (baseUnderAttack) {
            isSoundPlay = false;
            sound.pause();
        }
    }

    public void soundPlay() {
        if (baseUnderAttack) {
            isSoundPlay = true;
            sound.start();
        }
    }

    public void setUnderAttack(boolean isUnderAttack, int side) {
        this.isUnderAttack[side] = isUnderAttack;
        if (this.isUnderAttack[0] | this.isUnderAttack[1] | this.isUnderAttack[2]) {
            if (!baseUnderAttack) {
                sound = MediaPlayer.create(activity, R.raw.base_under_attack);
                sound.start();
                sound.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        if (isSoundPlay) {
                            mp.start();
                        }
                    }
                });
            }
            baseUnderAttack = true;
        } else {
            if (baseUnderAttack)
                sound.release();
            baseUnderAttack = false;
        }
    }

    public boolean isUnderAttack() {
        return baseUnderAttack;
    }

    public void receiveDamage(float damage) {
        leftHP = leftHP - damage > 0 ? leftHP - damage : 0;
        LinearLayout.LayoutParams lParams1 = ((LinearLayout.LayoutParams) view.findViewById(R.id.hp).getLayoutParams());
        LinearLayout.LayoutParams lParams2 = ((LinearLayout.LayoutParams) view.findViewById(R.id.last_hp).getLayoutParams());
        lParams1.weight = hp - leftHP + 1;
        lParams2.weight = leftHP + 1;
        view.findViewById(R.id.hp).setLayoutParams(lParams1);
        view.findViewById(R.id.last_hp).setLayoutParams(lParams2);
        if (leftHP == 0) isDead = true;
    }

    public float getPercentHP() {
        return (leftHP / hp) * 100;
    }
}