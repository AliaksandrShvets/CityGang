package com.woodcock.citygang;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import com.woodcock.citygang.classes.Constants;

/**
 * Created by Alex on 29.06.2016.
 */
public class LanguageActivity extends Activity {
    SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.language);

        sp = getSharedPreferences(Constants.SP, MODE_PRIVATE);

        findViewById(R.id.l_ru).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sp.edit().putString("language", "ru").apply();
                onBackPressed();
            }
        });

        findViewById(R.id.l_en).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sp.edit().putString("language", "en").apply();
                onBackPressed();
            }
        });

        findViewById(R.id.l_us).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sp.edit().putString("language", "en").apply();
                onBackPressed();
            }
        });

        findViewById(R.id.l_es).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sp.edit().putString("language", "es").apply();
                onBackPressed();
            }
        });

        findViewById(R.id.l_de).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sp.edit().putString("language", "de").apply();
                onBackPressed();
            }
        });

        findViewById(R.id.l_pt).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sp.edit().putString("language", "pt").apply();
                onBackPressed();
            }
        });

        findViewById(R.id.l_fr).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sp.edit().putString("language", "fr").apply();
                onBackPressed();
            }
        });
    }
}
