package com.woodcock.citygang;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.woodcock.citygang.classes.Constants;

/**
 * Created by Alex on 29.06.2016.
 */
public class Preloader extends Activity{
    Handler handler;
    Runnable runnable;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.preloader);
        handler = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {
                onBackPressed();
            }
        };
        handler.postDelayed(runnable, 2400);
    }

    @Override
    protected void onPause() {
        super.onPause();
        onBackPressed();
    }

    @Override
    public void onBackPressed() {
        if (getSharedPreferences(Constants.SP, MODE_PRIVATE).getString("language", "").isEmpty()) {
            getSharedPreferences(Constants.SP, MODE_PRIVATE).edit().putString("language", "default").apply();
            startActivity(new Intent(Preloader.this, LanguageActivity.class));
        }
        handler.removeCallbacks(runnable);
        super.onBackPressed();
    }
}
