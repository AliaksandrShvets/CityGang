package com.woodcock.citygang;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.woodcock.citygang.adapters.HierarchyPagerAdapter;
import com.woodcock.citygang.classes.Analytics;
import com.woodcock.citygang.classes.Business;
import com.woodcock.citygang.classes.Constants;
import com.woodcock.citygang.classes.PersonageBar;
import com.woodcock.citygang.custom_view.AdjustableImageView;
import com.woodcock.citygang.fragments.GamePlay;
import com.woodcock.citygang.fragments.HierarchyItem;
import com.woodcock.citygang.fragments.PersonageInfo;

import java.util.ArrayList;
import java.util.Locale;

public class MainUI extends FragmentActivity {

    InterstitialAd mInterstitialAd;

    View field;
    float HEIGHT_FIELD;
    int height, width;

    //fragments
    GamePlay game;
    PersonageInfo personageInfo;

    //viewpager
    ViewPager hierarchyPager;
    HierarchyPagerAdapter hierarchyPagerAdapter;
    ArrayList<HierarchyItem> items = new ArrayList<>();
    ArrayList<Business> businesses = new ArrayList<>();
    int lastPage = 0;

    ArrayList<PersonageBar> bar = new ArrayList<>(); //personages bottom
    private boolean isPagerNull = true, //for first initialise business pager
            isPersonageInfoOpened = false, //for check personage info window state
            isGameStarted = false; //for check game window state
    Analytics analytics; //analytics
    SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        startActivity(new Intent(MainUI.this, Preloader.class));
        setContentView(R.layout.main);

        analytics = new Analytics(this);

        initInterstitial();

        //----->SharedPreferences-------------------------------------------------------------------
        //getSharedPreferences(Constants.TUTORIAL, MODE_PRIVATE).edit().putBoolean("game_over", true).commit();
        sp = getSharedPreferences(Constants.APP_PREFERENCES, MODE_PRIVATE);
        if (sp.getBoolean(Constants.SP_FIRST_START, true)) {
            sp.edit().putBoolean(Constants.SP_FIRST_START, false).apply();
            getAnalytics().Send(Constants.CATEGORY_FUNNEL, "1_FirstStart", "Result");
        }

        //----->get display metrics & setting layout params ----------------------------------------
        DisplayMetrics display = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(display);
        width = display.widthPixels;
        height = display.heightPixels;
        HEIGHT_FIELD = (int) (height * 0.867f);

        field = findViewById(R.id.field);
        field.getLayoutParams().height = (int) HEIGHT_FIELD;
        findViewById(R.id.shadow).getLayoutParams().height = (int) (height - HEIGHT_FIELD);
        findViewById(R.id.pager_container).getLayoutParams().height = (int) (height - HEIGHT_FIELD);
        findViewById(R.id.pager_container).setY(HEIGHT_FIELD);

        //----->set hierarchy pager ----------------------------------------------------------------
        hierarchyPager = (ViewPager) findViewById(R.id.hierarchy_pager);
        fillBusinesses();
        findViewById(R.id.start_game).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startGame(lastPage);
            }
        });

        findViewById(R.id.m_language).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainUI.this, LanguageActivity.class));
            }
        });

        //----->set personage bar ------------------------------------------------------------------
        fillPersonageBar();

        //----->end game----------------------------------------------------------------------------
        if (getSharedPreferences(Constants.TUTORIAL, MODE_PRIVATE).getBoolean("the_end", false)) {
            showEnd();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        String language = getSharedPreferences(Constants.SP, MODE_PRIVATE).getString("language", "");
        ((Button) findViewById(R.id.m_language)).setText(language);
        if (!language.equals("default"))
            setLanguage(language);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        getAnalytics().Send(Constants.CATEGORY_TEST, "onDestroy", "MainUI");
        if (sp.getInt("sendNotification", 0) < 2) {
            startService(new Intent(this, NotificationService.class));
        }
    }

    @Override
    public void onBackPressed() {
        if (getFragmentManager().getBackStackEntryCount() > 0) {
            if ((getFragmentManager().getBackStackEntryAt(getFragmentManager().getBackStackEntryCount() - 1) + "").contains("game_play")) {
                if (game.back()) {
                    if (mInterstitialAd.isLoaded()) {
                        mInterstitialAd.show();
                    }
                    getFragmentManager().popBackStack();
                }
            } else
                getFragmentManager().popBackStack();
        } else
            super.onBackPressed();
    }

    private void setLanguage(String language) {
        Locale mNewLocale;
        mNewLocale = new Locale(language);
        Locale.setDefault(mNewLocale);
        android.content.res.Configuration config = new android.content.res.Configuration();
        config.locale = mNewLocale;
        getResources().updateConfiguration(config, getResources().getDisplayMetrics());
    }

    //----->Show methods----------------------------------------------------------------------------
    public void showNotification() {
        Context context = getApplicationContext();

        Intent notificationIntent = new Intent(context, MainUI.class);
        PendingIntent contentIntent = PendingIntent.getActivity(context,
                0, notificationIntent,
                PendingIntent.FLAG_CANCEL_CURRENT);

        Resources res = context.getResources();
        Notification.Builder builder = new Notification.Builder(context);

        builder.setContentIntent(contentIntent)
                .setSmallIcon(R.drawable.icon_mex)
                .setLargeIcon(BitmapFactory.decodeResource(res, R.drawable.icon_mex))
                .setTicker("Бойцы ждут тебя!")
                .setWhen(System.currentTimeMillis())
                .setAutoCancel(true)
                .setContentTitle("Бойцы не долго продержатся без тебя.")
                .setContentText("Бой закончится поражением, еще есть время исправить ситуацию.");

        Notification notification = builder.build();
        NotificationManager notificationManager = (NotificationManager) context
                .getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(101, notification);
    }

    public void showEnd() {
        ((AdjustableImageView) findViewById(R.id.the_end)).setImageResource(R.drawable.final_screen3);
        findViewById(R.id.the_end).setBackgroundColor(Color.BLACK);
        findViewById(R.id.the_end).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
    }

    //----->ANALYTICS <-----------------------------------------------------------------------------
    public Analytics getAnalytics() {
        return analytics;
    }

    //----->ANIMATIONS OF TUTORIAL <----------------------------------------------------------------
    public void gameResume() {
        game.onResume();
    }

    public void sendVote() {
        game.sendVote();
    }

    //----->Initialisation methods------------------------------------------------------------------
    public void fillPersonageBar() {
        for (int i = 0; i < 5; i++) {
            bar.add(new PersonageBar(i));
            LayoutInflater ltInflater = getLayoutInflater();
            View view = ltInflater.inflate(R.layout.item_personage_bar, null, false);
            view.setTag(i + "");
            bar.get(i).setView(view, Typeface.createFromAsset(getAssets(), "CompactBold.ttf"));
            ((LinearLayout) findViewById(R.id.pager_container)).addView(view);
            ((LinearLayout.LayoutParams) view.getLayoutParams()).weight = 1;

            view.setOnTouchListener(new View.OnTouchListener() {
                float startX, startY, nextX, nextY, leftPartX, rightPartX;
                String side;

                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if (isGameStarted) {
                        if (bar.get(Integer.parseInt(v.getTag() + "")).isPersonageEnabled()) {
                            switch (event.getAction()) {
                                case MotionEvent.ACTION_DOWN:
                                    startX = v.getWidth() / 2;
                                    startY = v.getHeight() / 2;
                                    leftPartX = ((v.getWidth() * (-Integer.parseInt(v.getTag() + "") + 1)) - v.getWidth() * 0.5f) / v.getHeight() / 3;
                                    rightPartX = ((v.getWidth() * (-Integer.parseInt(v.getTag() + "") + 1)) + v.getWidth() * 2.5f) / v.getHeight() / 3;
                                    side = "cancel";
                                    setArrow(side);
                                    ((AdjustableImageView) findViewById(R.id.shadow)).setImageResource(bar.get(Integer.parseInt(v.getTag() + "")).getImage());
                                    findViewById(R.id.shadow).setVisibility(View.VISIBLE);
                                    break;

                                case MotionEvent.ACTION_MOVE:
                                    nextX = event.getX() - startX;
                                    nextY = startY - event.getY();
                                    findViewById(R.id.shadow).setX(v.getX() + nextX);
                                    findViewById(R.id.shadow).setY(findViewById(R.id.pager_container).getY() - nextY - v.getHeight());
                                    if (Math.sqrt(nextX * nextX + nextY * nextY) > v.getHeight() / 1) {
                                        if (nextX < nextY * leftPartX) {
                                            side = "left";
                                        } else if (nextX < nextY * rightPartX) {
                                            side = "center";
                                        } else {
                                            side = "right";
                                        }
                                    } else {
                                        side = "cancel";
                                    }
                                    setArrow(side);
                                    break;

                                case MotionEvent.ACTION_CANCEL:
                                    setArrow("hide");
                                    findViewById(R.id.shadow).setVisibility(View.GONE);
                                    break;

                                case MotionEvent.ACTION_UP:
                                    if (side != "cancel") {
                                        bar.get(Integer.parseInt(v.getTag() + "")).startCooldown();
                                        addPersonage(bar.get(Integer.parseInt(v.getTag() + "")).getType(), side);
                                    } else
                                        setArrow("hide");
                                    findViewById(R.id.shadow).setVisibility(View.GONE);
                                    break;
                            }
                        }
                    } else {
                        openPersonageInfo(Integer.parseInt(v.getTag() + ""));
                    }
                    return true;
                }
            });
        }
    }

    public void disableAll() {
        for (int i = 0; i < 5; i++) {
            bar.get(i).disable();
        }
    }

    public void resetPersonageBar() {
        for (int i = 0; i < 5; i++) {
            bar.get(i).reset();
        }
    }

    public void updateCooldown() {
        for (int i = 0; i < 5; i++) {
            bar.get(i).updateCooldown();
        }
    }

    public void fillBusinesses() {
        lastPage = getSharedPreferences(Constants.SP, MODE_PRIVATE).getInt(Constants.SP_COUNT_OPENED_BUSINESSES, 1);
        for (int i = 0; i < Constants.HIERARCHY_ITEMS_COUNT + 2; i++)
            items.add(HierarchyItem.newInstance(this, i));
        hierarchyPagerAdapter = new HierarchyPagerAdapter(getSupportFragmentManager(), items);
        hierarchyPager.setAdapter(hierarchyPagerAdapter);
        hierarchyPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                pageChange(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
        hierarchyPager.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                if (isPagerNull) {
                    isPagerNull = false;
                    lastPage = getSharedPreferences(Constants.SP, MODE_PRIVATE).getInt(Constants.SP_COUNT_OPENED_BUSINESSES, 1);
                    selectLastBusiness();
                    if (lastPage < 2) {
                        pageChange(0);
                    }
                }
            }
        });
    }

    public void pageChange(int position) {
        int x = position + 1 > Constants.HIERARCHY_ITEMS_COUNT ? Constants.HIERARCHY_ITEMS_COUNT : position + 1;
        findViewById(R.id.start_game).setEnabled(businesses.get(x).isEnabled());

        if (businesses.get(lastPage) != null) {
            if (businesses.get(lastPage).isEnabled()) {
                items.get(lastPage).setSelect(false);
            }
        }
        lastPage = position + 1 > Constants.HIERARCHY_ITEMS_COUNT ? Constants.HIERARCHY_ITEMS_COUNT : position + 1;
        if (items.get(lastPage) != null) {
            if (businesses.get(lastPage).isEnabled()) {
                items.get(lastPage).setSelect(true);
            }
        }
    }

    public void updateBusinesses() {
        int count = getSharedPreferences(Constants.SP, MODE_PRIVATE).getInt(Constants.SP_COUNT_OPENED_BUSINESSES, 1);
        businesses.clear();
        businesses.add(null);
        for (int i = 1; i < Constants.HIERARCHY_ITEMS_COUNT + 1; i++) {
            businesses.add(new Business(count >= i, getSharedPreferences(Constants.SP, MODE_PRIVATE).getInt(Constants.SP_COUNT_STARS, 0)));
        }
        businesses.add(null);

        for (int i = 0; i < items.size(); i++) {
            HierarchyItem item = items.get(i);
            //проверить почему нал проверяю так то в дону строку можно написать
            if (businesses.get(i) != null) {
                item.update(businesses.get(i).isEnabled());
            }
        }
    }

    public void selectLastBusiness() {
        updateBusinesses();
        hierarchyPager.setCurrentItem(getSharedPreferences(Constants.SP, MODE_PRIVATE).getInt(Constants.SP_COUNT_OPENED_BUSINESSES, 1) - 1);
    }

    public void openPersonageInfo(int id) {
        if (!isPersonageInfoOpened) {
            personageInfo = new PersonageInfo().newInstance(bar.get(id).getName());
            getFragmentManager().beginTransaction()
                    .add(R.id.field_frame_container, personageInfo)
                    .addToBackStack("personage")
                    .commit();

            isPersonageInfoOpened = true;
        } else {
            if (personageInfo != null) {
                personageInfo.setNewInstance(bar.get(id).getName());
            }
        }
    }

    private void initInterstitial() {
        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId(getString(R.string.Interstitial_ad_unit_id));

        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                requestNewInterstitial();
            }
        });

        requestNewInterstitial();
    }

    private void requestNewInterstitial() {
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice("SEE_YOUR_LOGCAT_TO_GET_YOUR_DEVICE_ID")
                .build();

        mInterstitialAd.loadAd(adRequest);
    }

    public void setPersonageInfoOpenedFalse() {
        isPersonageInfoOpened = false;
    }

    //----->Game------------------------------------------------------------------------------------
    public void startGame(int id) {
        if (!isGameStarted) {
            setGameStarted(true);
            game = new GamePlay().newInstance(id);
            getFragmentManager().beginTransaction()
                    .replace(R.id.field_frame_container, game)
                    .addToBackStack("game_play")
                    .commit();
        }
    }

    public void setGameStarted(boolean isGameStarted) {
        this.isGameStarted = isGameStarted;
    }

    public void setArrow(String side) {
        game.setArrow(side);
    }

    public void addPersonage(String position, String way) {
        game.addPersonage(position, way);
    }

    public MediaPlayer getSound(Integer sound) {
        return MediaPlayer.create(this, sound);
    }
}