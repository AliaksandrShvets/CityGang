package com.woodcock.citygang.classes;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.woodcock.citygang.R;

import java.util.ArrayList;

/**
 * Created by Alex on 17.02.2016.
 */
public class Way {
    Activity activity;
    Personage furtherAlly, nearestEnemy;
    Base baseAlly, baseEnemy;
    ArrayList<Personage> personages = new ArrayList<>();
    Float furtherAllyY = 1f, nearestEnemyY = 0f, x;
    Integer widthContainer, heightContainer, width, length;
    char road;

    public Way(Activity activity, char road, Integer length, Base baseAlly, Base baseEnemy) {
        this.activity = activity;
        this.road = road;
        this.length = length;
        this.baseAlly = baseAlly;
        this.baseEnemy = baseEnemy;
        widthContainer = activity.findViewById(R.id.game_field).getWidth();
        heightContainer = activity.findViewById(R.id.game_field).getHeight();
        switch (road) {
            case 'l':
                x = -0.158f;
                break;
            case 'c':
                x = 0f;
                break;
            case 'r':
                x = 0.158f;
                activity.findViewById(R.id.range_container).getLayoutParams().width = widthContainer;
                ((RelativeLayout) activity.findViewById(R.id.game_field)).addView(baseEnemy.getView());
                baseEnemy.getView().getLayoutParams().width = widthContainer / 2;
                baseEnemy.getView().setX(widthContainer / 4);
                baseEnemy.getView().setY(Constants.BASE_ENEMY_Y);
                ((RelativeLayout) activity.findViewById(R.id.game_field)).addView(baseAlly.getView());
                baseAlly.getView().getLayoutParams().width = widthContainer / 2;
                baseAlly.getView().setX(widthContainer / 4);
                baseAlly.getView().setY(heightContainer - 20);
                break;
        }
    }

    public void addPersonage(String type, boolean isAlly, Bitmap[] run, Bitmap[] battle) {
        widthContainer = activity.findViewById(R.id.game_field).getWidth();
        width = (int) (widthContainer / activity.getResources().getInteger(R.integer.size) * 1.2f);
        LayoutInflater lInflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        //personage
        View view = lInflater.inflate(R.layout.item_personage, ((RelativeLayout) activity.findViewById(R.id.game_field)), false);
        view.getLayoutParams().width = width;
        view.getLayoutParams().height = width;
        if (isAlly) {
            view.setY(heightContainer - width / 2);
            ((LinearLayout.LayoutParams) view.findViewById(R.id.padding_down).getLayoutParams()).weight = 6;
        } else {
            view.setY(-width / 2);
            ((LinearLayout.LayoutParams) view.findViewById(R.id.padding_up).getLayoutParams()).weight = 15;
        }
        ((RelativeLayout) activity.findViewById(R.id.game_field)).addView(view);
        //shot animation
        View rangeIndicator = lInflater.inflate(R.layout.item_range_indicator, ((RelativeLayout) activity.findViewById(R.id.range_container)), false);
        rangeIndicator.getLayoutParams().width = width / 2;
        rangeIndicator.getLayoutParams().height = width / 30;
        rangeIndicator.setX(widthContainer / 2 + widthContainer * x - width / 2 + width / 4);
        ((RelativeLayout) activity.findViewById(R.id.range_container)).addView(rangeIndicator);
        Personage personage = new Personage(activity, type, view, rangeIndicator, run, battle, isAlly, widthContainer / 2 + heightContainer * x - width / 2);
        personages.add(personage);
    }

    public int run() {
        boolean baseUnderAttack = false;
        int side = road == 'l' ? 0 : road == 'c' ? 1 : 2;
        for (int i = 0; i < personages.size(); i++) {
            if (personages.get(i).isInBattle()) {
                if (personages.get(i).getEnemy() != null)
                    personages.get(i).getEnemy().receiveDamage(personages.get(i).getDamage());
                else {
                    if (personages.get(i).isAlly())
                        baseEnemy.receiveDamage(personages.get(i).getDamage());
                    else {
                        baseAlly.receiveDamage(personages.get(i).getDamage());
                        baseUnderAttack = true;
                    }
                }
            } else
                move(personages.get(i));
            personages.get(i).loadBitmap();
        }
        refresh();
        baseAlly.setUnderAttack(baseUnderAttack, side);

        for (Personage personage : personages)
            checkForBattle(personage, length);
        if (baseAlly.isDead()) return 1;
        if (baseEnemy.isDead()) return 2;
        return 0;
    }

    public void move(Personage personage) {
        View v = personage.getView();
        View range = personage.getRangeIndicator();
        v.setY(personage.getPassage() * heightContainer-width/2);
        range.setY(personage.getPassage() * heightContainer + (personage.isAlly() ? -1 : 1) * heightContainer * personage.getRange() / length - range.getHeight() / 2);
        switch (road) {
            case 'l':
                v.setX(personage.getX() - (float) Math.sin((v.getY() + width / 2) / (float) heightContainer * Math.PI) * heightContainer * 0.065f);
                range.setX(personage.getX() - (float) Math.sin((range.getY() + (personage.isAlly() ? -1 : 1) * width / 4) / (float) heightContainer * Math.PI) * heightContainer * 0.065f + width / 4);
                break;
            case 'r':
                v.setX(personage.getX() + (float) Math.sin((v.getY() + width / 2) / (float) heightContainer * Math.PI) * heightContainer * 0.065f);
                range.setX(personage.getX() + (float) Math.sin((range.getY() + (personage.isAlly() ? -1 : 1) * width / 4) / (float) heightContainer * Math.PI) * heightContainer * 0.065f + width / 4);
                break;
        }
        float newPassage = (personage.getPassage()+(personage.isAlly() ? -1 : 1)*(personage.getSpeed()/length));
        personage.setPassage(newPassage);
    }

    public void checkForBattle(Personage personage, Integer length) {
        if (personage.isInBattle() & !personages.contains(personage.getEnemy()) & personage.getEnemy() != null) {
            personage.setInBattle(false);
        }
        if (!personage.isInBattle()) {
            if (personage.isAlly()) {
                if (Math.abs(nearestEnemyY - personage.getPassage()) - personage.getRange() / (float) length < 0) {
                    personage.setInBattle(true);
                    personage.setEnemy(nearestEnemy);
                }
            } else {
                if (Math.abs(personage.getPassage() - furtherAllyY) - personage.getRange() / (float) length < 0) {
                    personage.setInBattle(true);
                    personage.setEnemy(furtherAlly);
                }
            }
        }
    }

    public void refresh() {
        for (int i = 0; i < personages.size(); i++) {
            Personage personage = personages.get(i);
            if (personage.isDead()) {
                personage.setInBattle(false);
                ((RelativeLayout) activity.findViewById(R.id.game_field)).removeView(personage.getView());
                ((RelativeLayout) activity.findViewById(R.id.range_container)).removeView(personage.getRangeIndicator());
                personages.remove(personage);
                furtherAlly = null;
                nearestEnemy = null;
                furtherAllyY = 1f;
                nearestEnemyY = 0f;
            }
        }

        for (int i = 0; i < personages.size(); i++) {
            Personage personage = personages.get(i);
            if (personage.isAlly()) {
                if ((personage.getPassage()) < furtherAllyY) {
                    furtherAllyY = personage.getPassage();
                    furtherAlly = personage;
                }
            } else {
                if (personage.getPassage() > nearestEnemyY) {
                    nearestEnemyY = personage.getPassage();
                    nearestEnemy = personage;
                }
            }
        }
    }

    public void generateBitmap() {
        for (int i = 0; i < personages.size(); i++)
            if (personages.get(i) != null)
                personages.get(i).nextBitmap();
    }

    public void soundPause(){
        for (Personage v : personages) v.soundPause();
    }

    public void soundPlay(){
        for (Personage v : personages) v.soundPlay();
    }

    public void soundRelease() {
        for (Personage v : personages) v.soundRelease();
    }

    public int getLength() {
        return length;
    }

    public float getFurtherAllyY() {
        return furtherAllyY;
    }

    public float getNearestEnemyY() {
        return nearestEnemyY;
    }

    public Personage getFurtherAlly() {
        return furtherAlly;
    }

    public Personage getNearestEnemy() {
        return nearestEnemy;
    }

    public ArrayList<Personage> getVehicle() {
        return personages;
    }

    public int getCountEnemy()
    {
        int count=0;
        for(Personage pers:personages)
            if(!pers.isAlly())
                count++;
        return count;
    }

    public int getCountAlly()
    {
        int count=0;
        for(Personage pers:personages)
            if(pers.isAlly())
                count++;
        return count;
    }

    public void setHighSpeed(){
        for(Personage pers:personages){
            pers.setHighSpeed();
        }
    }
}
