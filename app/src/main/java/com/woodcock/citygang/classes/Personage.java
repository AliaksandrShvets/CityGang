package com.woodcock.citygang.classes;

import android.app.Activity;
import android.graphics.Bitmap;
import android.media.MediaPlayer;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.woodcock.citygang.MyApplication;
import com.woodcock.citygang.R;

import java.util.Random;

/**
 * Created by Alex on 16.02.2016.
 */

public class Personage {
    Activity activity;

    private View view, rangeIndicator, hpLeft;
    private Integer range;
    private float speed, damage, hp, leftHP;
    private boolean isAlly, inBattle = false, isDead = false, isSoundPlay = true;
    private Personage enemy;
    private Float x, passage = 0f;
    private String type;
    private MediaPlayer sound;
    private int soundId;

    //lдля sharedpreference
    public static final String APP_PREFERENCES = "mysettings";

    //FOR SPRITE
    private Bitmap run, battle, nowBitmap, bitmap;
    private int n = -1;
    private float spriteLengthBattle, spriteLengthRun, spriteLength;

    //google analytics
    private Tracker mTracker;

    public Personage(Activity activity, String type, View view, View rangeIndicator, Bitmap[] run, Bitmap[] battle, boolean isAlly, float x) {

        MyApplication application = (MyApplication) activity.getApplication();
        mTracker = application.getTracker(MyApplication.TrackerName.APP_TRACKER);
        mTracker.enableAdvertisingIdCollection(true);

        this.activity = activity;
        this.view = view;
        this.type = type;
        this.rangeIndicator = rangeIndicator;
        hpLeft = view.findViewById(R.id.hp);
        int spriteId = 0;
        if (!isAlly) {
            view.findViewById(R.id.hp).setBackgroundResource(R.drawable.hp_enemy);
            ((ImageView) rangeIndicator.findViewById(R.id.image)).setImageResource(R.drawable.range_indicator_enemy);
        } else passage = 1f;
        this.isAlly = isAlly;
        this.x = x;
        view.setX(x);
        switch (type) {
            case "LT":
                speed = 7.667f;
                range = 250;
                damage = 2.875f;
                hp = 302.06f;
                leftHP = 302.06f;
                soundId = R.raw.personage_assassin;
                this.spriteLengthRun = 13;
                if (isAlly)
                    this.spriteLengthBattle = 40;
                else
                    this.spriteLengthBattle = 41;
                spriteId = 0;
                break;
            case "ST":
                speed = 4.14f;
                range = 500;
                damage = 1.577f;
                hp = 469.67f;
                leftHP = 469.67f;
                soundId = R.raw.personage_nigger;
                this.spriteLengthRun = 8;
                if (isAlly)
                    this.spriteLengthBattle = 17;
                else
                    this.spriteLengthBattle = 28;
                spriteId = 1;
                break;
            case "TT":
                speed = 2;
                range = 750;
                damage = 1;
                hp = 673.62f;
                leftHP = 673.62f;
                soundId = R.raw.personage_mexican;
                this.spriteLengthRun = 10;
                this.spriteLengthBattle = 10;
                spriteId = 2;
                break;
            case "PT":
                speed = 1.7f;
                range = 1000;
                damage = 1.13f;
                hp = 479.82f;
                leftHP = 479.82f;
                soundId = R.raw.personage_soldier;
                this.spriteLengthRun = 12;
                this.spriteLengthBattle = 16;
                spriteId = 3;
                break;
            case "ART":
                speed = 1;
                range = 1250;
                damage = 1.5f;
                hp = 201.48f;
                leftHP = 201.48f;
                soundId = R.raw.personage_invalid;
                if (isAlly) {
                    this.spriteLengthRun = 15;
                    this.spriteLengthBattle = 20;
                } else {
                    this.spriteLengthRun = 14;
                    this.spriteLengthBattle = 29;
                }
                spriteId = 4;
                break;
        }
        if ((new Random().nextInt(100)) < 5) {
            if (activity.getSharedPreferences(Constants.TUTORIAL, activity.MODE_PRIVATE).getBoolean("isGoldPersonagesEnable", false)) {
                hp *= 1.4f;
                leftHP *= 1.4f;
                damage *= 1.4f;
                view.findViewById(R.id.hp).setBackgroundResource(R.drawable.hp_gold);
            }
        }
        this.run = run[spriteId + (isAlly ? 0 : 5)];
        this.battle = battle[spriteId + (isAlly ? 0 : 5)];
        spriteLength = spriteLengthRun;
        nowBitmap = this.run;
        sound = MediaPlayer.create(activity, soundId);
        sound.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                if (isSoundPlay)
                    mp.start();
            }
        });
    }

    public View getView() {
        return view;
    }

    public View getRangeIndicator() {
        return rangeIndicator;
    }

    public Personage getEnemy() {
        return enemy;
    }

    public void setEnemy(Personage personage) {
        this.enemy = personage;
    }

    public boolean isAlly() {
        return isAlly;
    }

    public void setInBattle(final boolean inBattle) {
        if (this.inBattle != inBattle)
            try {
                nowBitmap = inBattle ? battle : run;
                spriteLength = inBattle ? spriteLengthBattle : spriteLengthRun;
                n = -1;
                if (inBattle) {
                    isSoundPlay = true;
                    sound.start();
                } else {
                    isSoundPlay = false;
                    if (leftHP / hp < 0.045f)
                        isDead = true;
                }
            } catch (Exception e) {
            }
        this.inBattle = inBattle;
    }

    public void soundRelease() {
        if (sound != null)
            sound.release();
    }

    public void soundPause() {
        if (inBattle) {
            sound.pause();
            isSoundPlay = false;
        }
    }

    public void soundPlay() {
        if (inBattle) {
            sound.start();
            isSoundPlay = true;
        }
    }

    public boolean isInBattle() {
        return inBattle;
    }

    public boolean isDead() {
        return isDead;
    }

    public Float getX() {
        return x;
    }

    public Float getSpeed() {
        return speed;
    }

    public Integer getRange() {
        return range == null ? -3 : range;
    }

    public float getDamage() {
        return damage;
    }

    public Float getPassage() {
        return passage;
    }

    public void setPassage(Float passage) {
        this.passage = passage;
    }

    public void receiveDamage(float damage) {
        leftHP = leftHP - damage > 0 ? leftHP - damage : 0;
        refreshHP();
        if (leftHP == 0) {
            isDead = true;
            isSoundPlay = false;
        }
    }

    private void refreshHP() {
        hpLeft.setLayoutParams(new RelativeLayout.LayoutParams((int) (leftHP / hp * view.findViewById(R.id.line_hp).getWidth()), ViewGroup.LayoutParams.MATCH_PARENT));
    }

    public String getType() {
        return type;
    }

    public float getPercentHP() {
        return (leftHP / hp) * 100;
    }

    public void nextBitmap() {
        if (n < spriteLength - 1) n++;
        else n = 0;
        if ((0 + (int) (nowBitmap.getWidth() / spriteLength * n)) < 0) {
            //вставь сюда аналитику (nowBitmap.getWidth() = ..., spriteLength = ..., n = ...)
            mTracker.setScreenName("Error");
            mTracker.send(new HitBuilders.EventBuilder()
                    .setCategory("Width")
                    .setAction("nowBitmap.getWidth() " + nowBitmap.getWidth())
                    .setLabel("Result")
                    .build()
            );

            mTracker.setScreenName("Error");
            mTracker.send(new HitBuilders.EventBuilder()
                    .setCategory("spriteLength")
                    .setAction("spriteLength = " + spriteLength)
                    .setLabel("Result")
                    .build()
            );

            mTracker.setScreenName("Error");
            mTracker.send(new HitBuilders.EventBuilder()
                    .setCategory("n")
                    .setAction("n= " + n)
                    .setLabel("Result")
                    .build()
            );
        }
        bitmap = Bitmap.createBitmap(nowBitmap, ((0 + (int) (nowBitmap.getWidth() / spriteLength * n)) < 0 ? 0 : (0 + (int) (nowBitmap.getWidth() / spriteLength * n))), 0, (int) (nowBitmap.getWidth() / spriteLength), nowBitmap.getHeight());
    }

    public void loadBitmap() {
        ((ImageView) view.findViewById(R.id.personage)).setImageBitmap(bitmap);
    }

    public void setHighSpeed(){
        speed *= 5;
        damage *= 5;
    }
}