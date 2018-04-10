package com.woodcock.citygang.classes;

import android.app.Activity;
import android.graphics.Typeface;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.woodcock.citygang.MainUI;
import com.woodcock.citygang.R;
import com.woodcock.citygang.custom_view.AdjustableImageView;

/**
 * Created by Alex on 16.02.2016.
 */
public class PersonageBar {
    View personageBarView;
    private String type, name;

    //cooldown
    private TextView cooldownText;
    private int imageId, cooldown;
    private int PERSONAGE_COOLDOWN = 1200;
    private boolean isPersonageEnable = true, isAllDisabled=false;

    public PersonageBar(int position) {
        switch (position) {
            case 0:
                name = "As";
                type = "LT";
                imageId = R.drawable.bar_item_assassin;
                break;
            case 1:
                name = "Ng";
                type = "ST";
                imageId = R.drawable.bar_item_nigger;
                break;
            case 2:
                name = "Mx";
                type = "TT";
                imageId = R.drawable.bar_item_mexican;
                break;
            case 3:
                name = "Sd";
                type = "PT";
                imageId = R.drawable.bar_item_soldier;
                break;
            case 4:
                name = "Iv";
                type = "ART";
                imageId = R.drawable.bar_item_invalid;
                break;
        }
    }

    public PersonageBar(String type, String name) {
        this.type = type;
        this.name = name;
        switch (name) {
            case "As":
                imageId = R.drawable.bar_item_assassin;
                break;
            case "Ng":
                imageId = R.drawable.bar_item_nigger;
                break;
            case "Mx":
                imageId = R.drawable.bar_item_mexican;
                break;
            case "Sd":
                imageId = R.drawable.bar_item_soldier;
                break;
            case "Iv":
                imageId = R.drawable.bar_item_invalid;
                break;
        }
    }


    public void setView(View view, Typeface typeface) {
        personageBarView = view;
        ((AdjustableImageView)personageBarView.findViewById(R.id.image)).setImageResource(imageId);
        cooldownText = (TextView) personageBarView.findViewById(R.id.cooldown);
        cooldownText.setTypeface(typeface);
    }

    public int getImage() {
        return imageId;
    }

    public String getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public void disable(){
        isAllDisabled = true;
        isPersonageEnable = false;
        cooldownText.setText("");
        personageBarView.findViewById(R.id.image).setAlpha(0.3f);
    }

    public void reset() {
        isAllDisabled = false;
        isPersonageEnable = true;
        cooldownText.setText("");
        personageBarView.findViewById(R.id.image).setAlpha(1f);
    }

    public void updateCooldown() {
        if (!isPersonageEnable&!isAllDisabled) {
            if (cooldown > 0) {
                cooldown--;
                if (cooldown % 20 == 0) {
                    cooldownText.setText("" + cooldown / 20);
                }
            } else {
                isPersonageEnable=true;
                cooldownText.setText("");
                personageBarView.findViewById(R.id.image).setAlpha(1f);
            }
        }
    }

    public void startCooldown() {
        isPersonageEnable = false;
        cooldown = PERSONAGE_COOLDOWN;
        cooldownText.setText("" + PERSONAGE_COOLDOWN / 20);
        personageBarView.findViewById(R.id.image).setAlpha(0.3f);
    }

    public boolean isPersonageEnabled(){
        return isPersonageEnable;
    }
}