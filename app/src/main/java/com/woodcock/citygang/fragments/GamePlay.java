package com.woodcock.citygang.fragments;

import android.app.Fragment;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.AnimRes;
import android.text.Editable;
import android.text.Layout;
import android.text.Spanned;
import android.text.TextWatcher;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.woodcock.citygang.MainUI;
import com.woodcock.citygang.R;
import com.woodcock.citygang.animations.AnimateView;
import com.woodcock.citygang.classes.AI;
import com.woodcock.citygang.classes.Auto;
import com.woodcock.citygang.classes.Base;
import com.woodcock.citygang.classes.Constants;
import com.woodcock.citygang.classes.PersonageBar;
import com.woodcock.citygang.classes.Way;
import com.woodcock.citygang.classes.animation.RotateAnimationCogwheel;
import com.woodcock.citygang.custom_view.AdjustableImageView;
import com.woodcock.citygang.database.DBStatistic;

import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Alex on 06.04.2016.
 */
public class GamePlay extends Fragment {

    public static final int TUTORIAL_ARROW_SIZE = 8;
    View view;

    private Timer mTimer = new Timer();
    private MyTimerTask myTimerTask;
    private Timer backgroundAnimationTimer;

    //Game settings
    private static int businessId;
    private static int PLANC_MAX = 230, ROUND_COUNT = 1, SECONDS_COUNT = 1800;
    private int tics = 99999, round = 1, seconds = SECONDS_COUNT;
    private boolean isFirstRun = true, isTimerPlay = true, isGameStart = false, isGameOver = false,
            isFirstBattle = false, isTechnicalLoss = false,
            isRoundVisible = true, isHighSpeed = false;

    //pause task
    Handler handlerPause;
    Runnable runnableLoss;

    //pat
    private int patCount = 0;
    private boolean isPat = false;

    //GameField subjects
    private Way leftWay, centerWay, rightWay, way;
    private Base baseAlly, baseEnemy;
    private ArrayList<PersonageBar> enemyVehicle = new ArrayList<>();

    private SQLiteDatabase db;
    AlphaAnimation show;

    //Background Animation
    float horizontalWay, verticalWay, sizeWay;
    float HEIGHT_FIELD, WIDTH_FIELD;
    ArrayList<Auto> verticalRoad, horizontalRoad;

    //Sounds
    MediaPlayer roundStart, roundFinish, cooldownFinish, winSound;
    boolean isRoundStartPlay = false, isRoundFinishPlay = false, isCooldownFinishPlay = false, isWinSoundPlay = false;

    //Sprites
    Bitmap spriteBattle[] = new Bitmap[10], spriteRun[] = new Bitmap[10];
    int roundImage[] = {R.drawable.round1, R.drawable.round2, R.drawable.round3, R.drawable.round4, R.drawable.round5, R.drawable.round6, R.drawable.round7, R.drawable.round8, R.drawable.round9};

    SharedPreferences sp;

    //google analytics
    private float winsPercent;

    //tutorial
    RelativeLayout layout; //tutorial container
    View shadowTop, shadowBottom; //hide top/bottom zones
    AdjustableImageView arrow; //tutorial arrow
    TextView text; //tutorial text
    int lastDialog = 0; //tutorial progress
    Integer top = 0, bottom = 0; //top/bottom borders
    boolean isUnderArrow = true, isTutorialOpened = false; //text alignment

    //training
    private int stage = 0;
    private boolean isWin = false;
    private int countGame = 0;

    private AI ai;
    private int countPersonage;
    private int mode;
    private int changeTime = 0;
    private int result;
    private boolean isLastSecond = false;

    //для показа голосовалки и
    private Boolean isSend;


    public static GamePlay newInstance(Integer id) {
        businessId = id;
        return new GamePlay();
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.game_play, null);
        handlerPause = new Handler();

        fillList();

        isSend = false;

        sp = getActivity().getSharedPreferences(Constants.APP_PREFERENCES, MODE_PRIVATE);
        stage = sp.getInt("stage", 0);

        if (!getActivity().getSharedPreferences(Constants.TUTORIAL, MODE_PRIVATE).getBoolean("isFirstBattle", false)) {
            getActivity().getSharedPreferences(Constants.TUTORIAL, MODE_PRIVATE).edit().putBoolean("isFirstBattle", true).apply();
            ((MainUI) getActivity()).getAnalytics().Send(Constants.CATEGORY_FUNNEL, "2_Battle started", "Result");
            isFirstBattle = true;
        }

        DBStatistic dbHelper = new DBStatistic(getActivity().getBaseContext());
        db = dbHelper.getWritableDatabase();
        Cursor cursor = db.query("mystat", null, null, null, null, null, null);
        int countWin = 0, countLose = 0, countRound = 0, sumRound = 0;
        winsPercent = 0;
        //для проверки в первых 10 боях
        int[] array_result = new int[2];
        int[] array_three_win = new int[3];
        int counter = 0, counter_three_win = 0;
        if (cursor.moveToFirst()) {
            do {
                if (cursor.getInt(1) == 1) {
                    countWin++;
                    sumRound += cursor.getInt(2);
                    countRound++;
                    isWin = true;

                    //заносим данные в массив для первых 10 боев
                    counter++;
                    if (counter % 2 == 0)
                        counter = 0;
                    array_result[counter] = 1;

                    //заносим данные в массив
                    counter_three_win++;
                    if (counter_three_win % 3 == 0)
                        counter_three_win = 0;
                    array_three_win[counter_three_win] = 1;

                } else {
                    countLose++;
                    sumRound += cursor.getInt(2);
                    countRound++;
                    isWin = false;

                    //заносим данные в массив для первых 10 боев
                    counter++;
                    if (counter % 2 == 0)
                        counter = 0;
                    array_result[counter] = 0;

                    //заносим данные в массив
                    counter_three_win++;
                    if (counter_three_win % 3 == 0)
                        counter_three_win = 0;
                    array_three_win[counter_three_win] = 0;
                }
            }
            while (cursor.moveToNext());
        }
        cursor.close();
        if (isFirstRun) {
            ContentValues contentValues = new ContentValues();
            contentValues.put("date", "" + (new GregorianCalendar()).getTime());
            contentValues.put("win", 1);
            contentValues.put("round", round);
            contentValues.put("premium", 0);
            db.insert("mystat", null, contentValues);
        }
        db.close();

        if (countLose + countWin > 0) {
            winsPercent = (countWin * 1.0f) / countRound;
        }

        if (countRound > 10) {
            countGame = countRound;
            if (isWin) {
                if (winsPercent >= 0.6f) {
                    if (stage < Constants.LAST_MODE_AI)
                        stage++;
                }
            }
            if (winsPercent < 0.6f && stage > 1) {
                stage--;
            }

            int count_battle = sp.getInt("count_battle", 0);
            Log.d("cunt", "count_battle " + count_battle);

            if (stage > Constants.LAST_MODE_AI)
                stage = Constants.LAST_MODE_AI;

            mode = stage;

            delayStart();
            randomMode();

        } else {
            int sum = array_result[0] + array_result[1];
            if (sum == 2)
                stage++;
            if (sum == 0)
                stage--;
            if (stage <= -1)
                stage = -1;
            mode = stage;
        }

        fillAi();

        if (mode > 2) {
            getActivity().getSharedPreferences(Constants.TUTORIAL, MODE_PRIVATE).edit().putBoolean("isGoldPersonagesEnable", true).apply();
        } else {
            getActivity().getSharedPreferences(Constants.TUTORIAL, MODE_PRIVATE).edit().putBoolean("isGoldPersonagesEnable", false).apply();
        }

        getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        ((TextView) view.findViewById(R.id.timer)).setTypeface(Typeface.createFromAsset(getActivity().getAssets(), "9364.ttf"));

        spriteBattle[0] = BitmapFactory.decodeResource(getResources(), R.drawable.sprite_ally_battle_assassin);
        spriteBattle[1] = BitmapFactory.decodeResource(getResources(), R.drawable.sprite_ally_battle_nigger);
        spriteBattle[2] = BitmapFactory.decodeResource(getResources(), R.drawable.sprite_ally_battle_mexican);
        spriteBattle[3] = BitmapFactory.decodeResource(getResources(), R.drawable.sprite_ally_battle_soldier);
        spriteBattle[4] = BitmapFactory.decodeResource(getResources(), R.drawable.sprite_ally_battle_invalid);
        spriteBattle[5] = BitmapFactory.decodeResource(getResources(), R.drawable.sprite_enemy_battle_assassin);
        spriteBattle[6] = BitmapFactory.decodeResource(getResources(), R.drawable.sprite_enemy_battle_nigger);
        spriteBattle[7] = BitmapFactory.decodeResource(getResources(), R.drawable.sprite_enemy_battle_mexican);
        spriteBattle[8] = BitmapFactory.decodeResource(getResources(), R.drawable.sprite_enemy_battle_soldier);
        spriteBattle[9] = BitmapFactory.decodeResource(getResources(), R.drawable.sprite_enemy_battle_invalid);
        spriteRun[0] = BitmapFactory.decodeResource(getResources(), R.drawable.sprite_ally_run_assassin);
        spriteRun[1] = BitmapFactory.decodeResource(getResources(), R.drawable.sprite_ally_run_nigger);
        spriteRun[2] = BitmapFactory.decodeResource(getResources(), R.drawable.sprite_ally_run_mexican);
        spriteRun[3] = BitmapFactory.decodeResource(getResources(), R.drawable.sprite_ally_run_soldier);
        spriteRun[4] = BitmapFactory.decodeResource(getResources(), R.drawable.sprite_ally_run_invalid);
        spriteRun[5] = BitmapFactory.decodeResource(getResources(), R.drawable.sprite_enemy_run_assassin);
        spriteRun[6] = BitmapFactory.decodeResource(getResources(), R.drawable.sprite_enemy_run_nigger);
        spriteRun[7] = BitmapFactory.decodeResource(getResources(), R.drawable.sprite_enemy_run_mexican);
        spriteRun[8] = BitmapFactory.decodeResource(getResources(), R.drawable.sprite_enemy_run_soldier);
        spriteRun[9] = BitmapFactory.decodeResource(getResources(), R.drawable.sprite_enemy_run_invalid);

        view.findViewById(R.id.pause).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isTimerPlay) {
                    v.setSelected(true);
                    onPause();
                    ((ImageView) view.findViewById(R.id.pause_image)).setImageResource(R.drawable.pause_start);
                } else {
                    v.setSelected(false);
                    onResume();
                    ((ImageView) view.findViewById(R.id.pause_image)).setImageResource(R.drawable.pause_pause);
                }
            }
        });

        //Sounds
        roundStart = ((MainUI) getActivity()).getSound(R.raw.round_start);
        roundFinish = MediaPlayer.create(getActivity(), R.raw.round_finish);
        cooldownFinish = MediaPlayer.create(getActivity(), R.raw.cooldown_finish);

        //preloader battle
        ScaleAnimation scaleAnimation = new ScaleAnimation(1.2f, 1f, 1.2f, 1f,
                ScaleAnimation.RELATIVE_TO_SELF, 0.5f,
                ScaleAnimation.RELATIVE_TO_SELF, 0.5f);
        scaleAnimation.setDuration(500);
        AlphaAnimation alphaAnimation = new AlphaAnimation(0, 1);
        alphaAnimation.setDuration(100);

        //all for animation
        ((ImageView) view.findViewById(R.id.map_background)).setImageResource(R.drawable.map1);
        ((ImageView) view.findViewById(R.id.map_shadows)).setImageResource(R.drawable.map1_shadows);
        ((ImageView) view.findViewById(R.id.map_layer_up)).setImageResource(R.drawable.map1_bridge);
        AlphaAnimation alpha = new AlphaAnimation(0, 1);
        alpha.setDuration(1000);
        view.findViewById(R.id.game_animation).startAnimation(alpha);
        view.post(new Runnable() {
            @Override
            public void run() {
                setBackgroundAnimation(0.003f, 0.612f, 0.016f);
            }
        });

        startGame();
        return view;
    }

    @Override
    public void onPause() {
        super.onPause();
        ((MainUI) getActivity()).getAnalytics().Send(Constants.CATEGORY_TEST, "onPause", "GamePlay");
        isTimerPlay = false;
        if (!isGameOver) {
            runnableLoss = new Runnable() {
                @Override
                public void run() {
                    ((MainUI) getActivity()).showNotification();
                    runnableLoss = new Runnable() {
                        @Override
                        public void run() {
                            isTechnicalLoss = true;
                        }
                    };
                    handlerPause.postDelayed(runnableLoss, 300000);
                }
            };
            handlerPause.postDelayed(runnableLoss, 300000);
            if (roundFinish.isPlaying()) {
                isRoundFinishPlay = true;
                roundFinish.pause();
            } else
                isRoundFinishPlay = false;
            if (roundStart.isPlaying()) {
                isRoundStartPlay = true;
                roundStart.pause();
            } else
                isRoundStartPlay = false;
            if (cooldownFinish.isPlaying()) {
                isCooldownFinishPlay = true;
                cooldownFinish.pause();
            } else
                isCooldownFinishPlay = false;
            if (leftWay != null) {
                leftWay.soundPause();
                centerWay.soundPause();
                rightWay.soundPause();
                baseAlly.soundPause();
            }
        } else {
            if (winSound.isPlaying()) {
                isWinSoundPlay = true;
                winSound.pause();
            } else {
                isWinSoundPlay = false;
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!isGameOver) {
            handlerPause.removeCallbacks(runnableLoss);
            if (!view.findViewById(R.id.pause).isSelected()) {
                isTimerPlay = true;
                if (isCooldownFinishPlay) cooldownFinish.start();
                if (isRoundStartPlay) roundStart.start();
                if (isRoundFinishPlay) roundFinish.start();
                if (leftWay != null) {
                    leftWay.soundPlay();
                    centerWay.soundPlay();
                    rightWay.soundPlay();
                    baseAlly.soundPlay();
                }
            } else {
                if (isWinSoundPlay) winSound.start();
            }
            ((MainUI) getActivity()).getAnalytics().Send(Constants.SCREEN_GAME);
            if (isTechnicalLoss) {
                getActivity().onBackPressed();
            }
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        backgroundAnimationTimer.cancel();
        mTimer.cancel();
        if (!isGameOver) {
            soundsRelease();
        }
        if (isFirstBattle) {
            ((MainUI) getActivity()).getAnalytics().Send(Constants.CATEGORY_FUNNEL, "3_Battle finished_" + (result == 2 ? "Win" : "Defeate"), "Result");
        }
        ((MainUI) getActivity()).selectLastBusiness();
        ((MainUI) getActivity()).resetPersonageBar();
        ((MainUI) getActivity()).setGameStarted(false);
    }

    public boolean back() {
        if (isGameOver) {
            return true;
        } else {
            if (isTutorialOpened) {
                layout.callOnClick();
            } else {
                if (isFirstBattle) {
                    ((MainUI) getActivity()).getAnalytics().Send(Constants.CATEGORY_FUNNEL, "3_Battle finished_ESC", "Result");
                }
                gameResult(false);
                result = 1;
                gameFinish();
            }
            return false;
        }
    }

    //--------------------> GAME <-------------------------------
    public void startGame() {
        myTimerTask = new MyTimerTask();
        mTimer.schedule(myTimerTask, 50, 50);
        view.findViewById(R.id.timer).setVisibility(View.VISIBLE);
        isGameStart = true;
        roundStart.start();
    }

    public void addPersonage(String type, String road) {
        way = road.equals("left") ? leftWay : road.equals("center") ? centerWay : rightWay;
        way.addPersonage(type, true, spriteRun, spriteBattle);
        setArrow("");
        /*result=2;
        gameResult(true);*/
    }

    public void setArrow(String side) {
        if (!isGameOver) {
            view.findViewById(R.id.way_arrow_gray).setVisibility(View.GONE);
            view.findViewById(R.id.way_arrow_left).setVisibility(View.GONE);
            view.findViewById(R.id.way_arrow_center).setVisibility(View.GONE);
            view.findViewById(R.id.way_arrow_right).setVisibility(View.GONE);
            switch (side) {
                case "left":
                    view.findViewById(R.id.way_arrow_left).setVisibility(View.VISIBLE);
                    break;
                case "center":
                    view.findViewById(R.id.way_arrow_center).setVisibility(View.VISIBLE);
                    break;
                case "right":
                    view.findViewById(R.id.way_arrow_right).setVisibility(View.VISIBLE);
                    break;
                case "cancel":
                    view.findViewById(R.id.way_arrow_gray).setVisibility(View.VISIBLE);
                    break;

            }
        }
    }

    public void fillList() {
        enemyVehicle.clear();
        for (int i = 0; i < 5; i++) {
            enemyVehicle.add(new PersonageBar(i));
        }
        //ai = new AI(enemyVehicle, 40, mode, countPersonage);
    }

    private void fillAi() {
        ArrayList<PersonageBar> personages = new ArrayList<PersonageBar>();
        for (int i = 0; i < 5; i++) {
            personages.add(new PersonageBar(i));
        }
        ai = new AI(enemyVehicle, 40, mode, 5);
    }

    class MyTimerTask extends TimerTask {
        @Override
        public void run() {
            if (!isFirstRun) {
                leftWay.generateBitmap();
                centerWay.generateBitmap();
                rightWay.generateBitmap();
            }
            if (getActivity() == null) {
                Log.d("foot", "error");
            } else
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (isTimerPlay) {
                            //create game field and ways on first run
                            if (isFirstRun) {
                                LayoutInflater lInflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                                View baseAllyView = lInflater.inflate(R.layout.item_base_hp, ((RelativeLayout) view.findViewById(R.id.game_field)), false);
                                baseAlly = new Base(getActivity(), baseAllyView, true);
                                View baseEnemyView = lInflater.inflate(R.layout.item_base_hp, ((RelativeLayout) view.findViewById(R.id.game_field)), false);
                                baseEnemyView.findViewById(R.id.hp).setBackgroundResource(R.drawable.hp_enemy);
                                baseEnemy = new Base(getActivity(), baseEnemyView, false);
                                leftWay = new Way(getActivity(), 'l', 5000, baseAlly, baseEnemy);
                                centerWay = new Way(getActivity(), 'c', 4000, baseAlly, baseEnemy);
                                rightWay = new Way(getActivity(), 'r', 5000, baseAlly, baseEnemy);
                                isFirstRun = false;
                                ((TextView) view.findViewById(R.id.timer)).setText("" + SECONDS_COUNT / 20);
                            }

                            if (isGameStart & ((round < ROUND_COUNT + 1) | isPat)) {
                                ((MainUI) getActivity()).updateCooldown();
                                seconds--;
                                if (seconds > 0) {
                                    if (seconds == 100)
                                        roundFinish.start();
                                    if (seconds % 20 == 0) {
                                        ((TextView) view.findViewById(R.id.timer)).setText("" + seconds / 20);
                                        if (seconds < 181) {
                                            showRound(seconds / 20);
                                        }
                                        if (isFirstBattle) {
                                            if ((SECONDS_COUNT - seconds) / 20 == 15) {
                                                view.findViewById(R.id.pause).callOnClick();
                                                initializeTutorialDialog();
                                                updateTutorialDialog();
                                            }
                                            if ((SECONDS_COUNT - seconds) / 20 == 20) {
                                                view.findViewById(R.id.pause).callOnClick();
                                                updateTutorialDialog();
                                            }
                                            if (baseAlly.isUnderAttack()) {
                                                if (lastDialog == 4) {
                                                    view.findViewById(R.id.pause).callOnClick();
                                                    updateTutorialDialog();
                                                }
                                            }
                                        }
                                    }
                                } else {
                                    if (round == ROUND_COUNT) {
                                        round = ROUND_COUNT + 1;
                                        ai.clearPersonageBar();
                                        ((TextView) view.findViewById(R.id.timer)).setText("0");
                                        ((MainUI) getActivity()).disableAll();
                                        setArrow("");
                                        getActivity().findViewById(R.id.shadow).setVisibility(View.GONE);
                                    } else {
                                        if (round == ROUND_COUNT - 1) {
                                            MediaPlayer.create(getActivity(), R.raw.round_last_finish).start();
                                        }
                                        seconds = SECONDS_COUNT;
                                        tics = PLANC_MAX;
                                        if (isPat) {
                                            isPat = false;
                                            round = ROUND_COUNT;
                                        } else {
                                            round++;
                                            showRound(round);
                                        }
                                    }
                                }
                            } else if (leftWay.getVehicle().size() == 0 & rightWay.getVehicle().size() == 0 & centerWay.getVehicle().size() == 0) {
                                isPat = true;
                                patCount = 0;
                                ((MainUI) getActivity()).resetPersonageBar();
                            }

                            //add enemy item_personage
                            if (tics > (PLANC_MAX) && seconds > 0) {
                                changeTime = 0;
                                tics = 0;
                                if (mode >= 12 && round < ROUND_COUNT - 1 && seconds < 290 && isLastSecond) {
                                    isLastSecond = false;
                                    if (new Random().nextInt(100) < 11)
                                        tics = PLANC_MAX - seconds + 20;
                                    else
                                        tics = PLANC_MAX + 1;
                                } else {
                                    isLastSecond = true;
                                    if (ai.getpersonagesize() > 0) {
                                        String[] info = ai.getInfo(leftWay, centerWay, rightWay, baseEnemy.getPercentHP(), round, seconds);
                                        switch (info[0]) {
                                            case "left":
                                                leftWay.addPersonage(ai.getVehicle(info[1]).getType(), false, spriteRun, spriteBattle);
                                                break;
                                            case "center":
                                                centerWay.addPersonage(ai.getVehicle(info[1]).getType(), false, spriteRun, spriteBattle);
                                                break;
                                            case "right":
                                                rightWay.addPersonage(ai.getVehicle(info[1]).getType(), false, spriteRun, spriteBattle);
                                                break;
                                        }
                                    } else {
                                        if (seconds > 300) {
                                            fillAi();
                                            tics = PLANC_MAX;
                                        }
                                    }
                                }
                            } else {
                                if (seconds < 300) {
                                    while (ai.getpersonagesize() > 0) {
                                        String[] info = ai.getInfo(leftWay, centerWay, rightWay, baseEnemy.getPercentHP(), round, seconds);
                                        if (info[0].equals("none"))
                                            break;
                                        switch (info[0]) {
                                            case "left":
                                                leftWay.addPersonage(ai.getVehicle(info[1]).getType(), false, spriteRun, spriteBattle);
                                                break;
                                            case "center":
                                                centerWay.addPersonage(ai.getVehicle(info[1]).getType(), false, spriteRun, spriteBattle);
                                                break;
                                            case "right":
                                                rightWay.addPersonage(ai.getVehicle(info[1]).getType(), false, spriteRun, spriteBattle);
                                                break;
                                        }
                                    }
                                }
                                tics++;
                            }

                            //high speed
                            if (round > ROUND_COUNT) {
                                if ((leftWay.getNearestEnemy() == null | leftWay.getFurtherAlly() == null) & (centerWay.getNearestEnemy() == null | centerWay.getFurtherAlly() == null) & (rightWay.getNearestEnemy() == null | rightWay.getFurtherAlly() == null))
                                    if (!isHighSpeed) {
                                        isHighSpeed = true;
                                        leftWay.setHighSpeed();
                                        centerWay.setHighSpeed();
                                        rightWay.setHighSpeed();
                                    }
                            }

                            //check technical win
                            if (round > ROUND_COUNT) {
                                if (leftWay.getCountEnemy() == 0 && centerWay.getCountEnemy() == 0 && rightWay.getCountEnemy() == 0 && (leftWay.getCountAlly() > 0 || centerWay.getCountAlly() > 0 || rightWay.getCountAlly() > 0)) {
                                    patCount++;
                                    if (patCount > 40) {
                                        result = 2;
                                    }
                                }
                            }

                            //check game over
                            if (result == 0) {
                                centerWay.run();
                                leftWay.run();
                                result = rightWay.run();
                            } else {
                                gameResult(result == 2 ? true : false);
                                cancel();
                                gameFinish();
                            }
                        }
                    }
                });
        }
    }

    private void gameFinish() {
        int count_win_AI = sp.getInt("count_win_ai", 0);
        int count_battle = sp.getInt("count_battle", 0);
        count_battle++;
        int count_money = sp.getInt("money_count", 0);
        SharedPreferences.Editor editor = sp.edit();
        editor.putInt("stage", mode);
        editor.apply();

        ContentValues contentValues = new ContentValues();
        contentValues.put("date", "" + (new GregorianCalendar()).getTime());
        contentValues.put("win", result == 1 ? 0 : 1);
        contentValues.put("round", round);
        contentValues.put("premium", 0);
        DBStatistic dbHelper = new DBStatistic(getActivity().getBaseContext());
        db = dbHelper.getWritableDatabase();
        db.insert("mystat", null, contentValues);
        db.close();

        String battleInfo = "";
        if (countGame < 25)
            battleInfo = "<25";
        if (countGame > 25 && countGame < 50)
            battleInfo = "25-50";
        if (countGame > 50 && countGame < 100)
            battleInfo = "50-100";
        if (countGame > 100 && countGame < 300)
            battleInfo = "100-300";
        if (countGame > 300)
            battleInfo = ">300";

        String moneyInfo = "";
        if (count_money == 0)
            moneyInfo = "0";
        if (count_money > 0 && count_money <= 500)
            moneyInfo = "1-500";
        if (count_money > 500 && count_money <= 2000)
            moneyInfo = "501-2000";
        if (count_money > 2000 && count_money <= 5000)
            moneyInfo = "2001-5000";
        if (count_money > 5000)
            moneyInfo = ">5000";

        ((MainUI) getActivity()).getAnalytics().Send(Constants.CATEGORY_GAME, "Rate Win/Lose for Battle (Battle " + battleInfo + ") : (WP " + (int) (winsPercent * 100) + ")", "Result");
        ((MainUI) getActivity()).getAnalytics().Send(Constants.CATEGORY_AI_WORKS, "Ai level for Battle (Battle" + battleInfo + ") : (AI " + stage + ")", "Result");
        ((MainUI) getActivity()).getAnalytics().Send(Constants.CATEGORY_MONETIZATION, "Money personage Battle (Battle" + battleInfo + ") : (money " + moneyInfo + ")", "Result");

        //сообщение на почту
        if (mode == Constants.LAST_MODE_AI && result == 2 && !isSend && !sp.getBoolean("isHelp", false)) {
            if (count_win_AI > 2) {
                sendMessage(getString(R.string.help_top), getString(R.string.help_bot), getString(R.string.help_mail), 1);
                sp.edit().putInt("count_win_ai", 0).apply();
            } else
                sp.edit().putInt("count_win_ai", count_win_AI + 1).apply();
        } else {
            if (mode == Constants.LAST_MODE_AI && result == 1)
                sp.edit().putInt("count_win_ai", 0).apply();
        }
        if (!isSend)
            startAnimationResult();
    }

    public void showRound(int round) {
        if (round <= roundImage.length) {
            ((ImageView) view.findViewById(R.id.round)).setImageResource(roundImage[round - 1]);

            final AlphaAnimation hide = new AlphaAnimation(1, 0);
            hide.setDuration(600);
            hide.setStartOffset(1200);
            hide.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    view.findViewById(R.id.round).setVisibility(View.GONE);
                    if (isRoundVisible) {
                        if (isRoundVisible) {
                            ((ImageView) view.findViewById(R.id.round)).setImageResource(R.drawable.round_add);
                            view.findViewById(R.id.round).startAnimation(show);
                        }
                        isRoundVisible = !isRoundVisible;
                    }
                }

                @Override
                public void onAnimationRepeat(Animation animation) {
                }
            });

            isRoundVisible = round == ROUND_COUNT;
            show = new AlphaAnimation(0, 1);
            show.setDuration(600);
            show.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                    view.findViewById(R.id.round).setVisibility(View.VISIBLE);
                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    view.findViewById(R.id.round).startAnimation(hide);
                }

                @Override
                public void onAnimationRepeat(Animation animation) {
                }
            });
            view.findViewById(R.id.round).startAnimation(show);
        }
    }

    public void sendVote() {

        isSend = true;
        sp.edit().putBoolean("isVoted", true).apply();
        mTimer.cancel();
        isTimerPlay = false;
        if (!isGameOver) {
            if (roundFinish.isPlaying()) {
                isRoundFinishPlay = true;
                roundFinish.pause();
            } else
                isRoundFinishPlay = false;
            if (roundStart.isPlaying()) {
                isRoundStartPlay = true;
                roundStart.pause();
            } else
                isRoundStartPlay = false;
            if (cooldownFinish.isPlaying()) {
                isCooldownFinishPlay = true;
                cooldownFinish.pause();
            } else
                isCooldownFinishPlay = false;
            if (leftWay != null) {
                leftWay.soundPause();
                centerWay.soundPause();
                rightWay.soundPause();
                baseAlly.soundPause();
            }
        }

        RelativeLayout rl_container = ((RelativeLayout) view.findViewById(R.id.rl_main_field));
        rl_container.removeAllViews();
        rl_container.removeAllViews();
        rl_container.setBackgroundColor(Color.BLACK);
        //фон
        ImageView iv_bg = new AdjustableImageView(view.getContext());
        iv_bg.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.karleone0));
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(rl_container.getWidth(), rl_container.getHeight());
        lp.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        iv_bg.setLayoutParams(lp);
        iv_bg.setAdjustViewBounds(true);
        rl_container.addView(iv_bg);

        TextView tv_help_top = new TextView(getActivity());
        lp = new RelativeLayout.LayoutParams((int) (WIDTH_FIELD * 0.9f), ViewGroup.LayoutParams.WRAP_CONTENT);
        lp.addRule(RelativeLayout.CENTER_HORIZONTAL);
        lp.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        tv_help_top.setLayoutParams(lp);
        tv_help_top.setTextColor(Color.WHITE);
        tv_help_top.setGravity(Gravity.CENTER_HORIZONTAL);
        tv_help_top.setText(getResources().getString(R.string.estimate_game_title));
        tv_help_top.setTextSize(TypedValue.COMPLEX_UNIT_PX, HEIGHT_FIELD / 18);
        tv_help_top.setY(HEIGHT_FIELD * 0.65f);
        tv_help_top.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), "CompactBold.ttf"));

        rl_container.addView(tv_help_top);

        TextView tv_vote = new TextView(getActivity());
        lp = new RelativeLayout.LayoutParams((int) (WIDTH_FIELD * 0.95f), ViewGroup.LayoutParams.WRAP_CONTENT);
        lp.addRule(RelativeLayout.CENTER_HORIZONTAL);
        tv_vote.setLayoutParams(lp);
        tv_vote.setBackgroundColor(Color.WHITE);
        tv_vote.setTextColor(Color.BLACK);
        tv_vote.setGravity(Gravity.CENTER_HORIZONTAL);
        tv_vote.setText(getResources().getString(R.string.estimate_game_yes));
        tv_vote.setTextSize(TypedValue.COMPLEX_UNIT_PX, HEIGHT_FIELD / 25);
        tv_vote.setY(HEIGHT_FIELD * 0.83f);
        tv_vote.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), "CompactBold.ttf"));

        rl_container.addView(tv_vote);

        TextView tv_not_vote = new TextView(getActivity());
        lp = new RelativeLayout.LayoutParams((int) (WIDTH_FIELD * 0.95f), ViewGroup.LayoutParams.WRAP_CONTENT);
        lp.addRule(RelativeLayout.CENTER_HORIZONTAL);
        tv_not_vote.setLayoutParams(lp);
        tv_not_vote.setBackgroundColor(Color.WHITE);
        tv_not_vote.setTextColor(Color.BLACK);
        tv_not_vote.setGravity(Gravity.CENTER_HORIZONTAL);
        tv_not_vote.setText(getResources().getString(R.string.estimate_game_no));
        tv_not_vote.setTextSize(TypedValue.COMPLEX_UNIT_PX, HEIGHT_FIELD / 25);
        tv_not_vote.setY(HEIGHT_FIELD * 0.92f);
        tv_not_vote.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), "CompactBold.ttf"));

        rl_container.addView(tv_not_vote);

        tv_vote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String appPackageName = getActivity().getPackageName();
                try {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                } catch (android.content.ActivityNotFoundException anfe) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
                }
                getActivity().onBackPressed();
            }
        });

        tv_not_vote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });
    }

    public void startAnimationResult() {
        RelativeLayout rl = (RelativeLayout) view.findViewById(R.id.game_field);
        rl.removeAllViews();
        AlphaAnimation alpha[] = {new AlphaAnimation(0, 1), new AlphaAnimation(0, 1), new AlphaAnimation(0, 1), new AlphaAnimation(0, 1), new AlphaAnimation(0, 1), new AlphaAnimation(0, 1), new AlphaAnimation(0, 1), new AlphaAnimation(0, 1), new AlphaAnimation(0, 1), new AlphaAnimation(0, 1), new AlphaAnimation(0, 1)};
        int idWin[] = {R.id.battle_result_container, R.id.battle_result_win_3, R.id.battle_result_win_4, R.id.battle_result_win_5, R.id.battle_result_win_6, R.id.battle_result_win_7, R.id.battle_result_win_8, R.id.battle_result_win_9, R.id.battle_result_win_10, R.id.battle_result_win_11, R.id.battle_result_win_12};
        int idImage[] = {R.drawable.result_win, R.drawable.result_win_layer1, R.drawable.result_win_layer2, R.drawable.result_win_layer5, R.drawable.result_win_layer4, R.drawable.result_win_layer7, R.drawable.result_win_layer3, R.drawable.result_win_layer10, R.drawable.result_win_layer6, R.drawable.result_win_layer9, R.drawable.result_win_layer8};
        for (int i = 0; i < alpha.length; i++) {
            alpha[i].setDuration(1000);
            alpha[i].setStartOffset(300 * i);
            if (i > 0)
                ((AdjustableImageView) view.findViewById(idWin[i])).setImageDrawable(getResources().getDrawable(idImage[i]));
            if (i == 2)
                ((AdjustableImageView) view.findViewById(idWin[i])).setScaleType(ImageView.ScaleType.CENTER_CROP);
            view.findViewById(idWin[i]).startAnimation(alpha[i]);
        }
    }

    public void gameResult(boolean isVictory) {
        if (!isGameOver) {
            isGameOver = true;
            isTimerPlay = false;

            view.findViewById(R.id.battle_result_container).setVisibility(View.VISIBLE);
            TranslateAnimation translateAnimation;
            if (isVictory) {
                int stars = getActivity().getSharedPreferences(Constants.SP, getActivity().MODE_PRIVATE).getInt(Constants.SP_COUNT_STARS + businessId, 0);
                if (stars < baseAlly.getPercentHP() / 20)
                    getActivity().getSharedPreferences(Constants.SP, MODE_PRIVATE).edit().putInt(Constants.SP_COUNT_STARS + businessId, (int) (baseAlly.getPercentHP() / 20)).commit();
                if (businessId == getActivity().getSharedPreferences(Constants.SP, getActivity().MODE_PRIVATE).getInt(Constants.SP_COUNT_OPENED_BUSINESSES, 1)) {
                    getActivity().getSharedPreferences(Constants.SP, MODE_PRIVATE).edit().putInt(Constants.SP_COUNT_OPENED_BUSINESSES, businessId + 1).commit();
                }
                view.findViewById(R.id.battle_result_win).setVisibility(View.VISIBLE);
                winSound = ((MainUI) getActivity()).getSound(R.raw.crowd_applaused);
                translateAnimation = new TranslateAnimation(0, 0, -view.findViewById(R.id.result_money).getHeight(), HEIGHT_FIELD);

                ((MainUI) getActivity()).getAnalytics().Send(Constants.CATEGORY_FUNNEL, "3_Battle business №" + businessId + " received", "Result");
            } else {
                winSound = ((MainUI) getActivity()).getSound(R.raw.crowd_sad);
                translateAnimation = new TranslateAnimation(0, 0, HEIGHT_FIELD, -view.findViewById(R.id.result_money).getHeight());
            }
            winSound.start();

            ((ImageView) view.findViewById(R.id.battle_result)).setImageResource(isVictory ? R.drawable.result_win : R.drawable.result_lose);
            ((TextView) view.findViewById(R.id.result_money)).setText(isVictory ? getResources().getString(R.string.battle_result_win) : getResources().getString(R.string.battle_result_lose));
            ((TextView) view.findViewById(R.id.result_money)).setTypeface(Typeface.createFromAsset(getActivity().getAssets(), "9364.ttf"));
            translateAnimation.setDuration(7000);
            translateAnimation.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    view.findViewById(R.id.result_money).setVisibility(View.GONE);
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
            view.findViewById(R.id.result_money).startAnimation(translateAnimation);
            view.findViewById(R.id.battle_result).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getActivity().onBackPressed();
                }
            });
        }
        soundsRelease();
        getActivity().getSharedPreferences(Constants.TUTORIAL, MODE_PRIVATE).edit().putBoolean("game_over", true).apply();
    }

    private void startSendAnimation(RelativeLayout container, int imageId, int width, int height, int rule, float y, final @AnimRes int animationID) {
        final ImageView imageView = new AdjustableImageView(view.getContext());
        imageView.setImageDrawable(getActivity().getResources().getDrawable(imageId));
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(width, height);
        lp.addRule(rule);
        imageView.setLayoutParams(lp);
        imageView.setAdjustViewBounds(true);
        imageView.setY(y);
        container.addView(imageView);

        final Animation animationTranslateT = AnimationUtils.loadAnimation(
                getActivity(), animationID);
        animationTranslateT.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                Animation anim = AnimationUtils.loadAnimation(getActivity(), animationID);
                anim.setAnimationListener(this);
                imageView.startAnimation(anim);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });
        imageView.startAnimation(animationTranslateT);
    }

    private void startSendAnimationCogwheel(RelativeLayout container, int imageId, int width, int height, float x, float y, float fromDegrees, float toDegrees, float pivotX, float pivotY, long duration) {
        View cogwheel = new View(view.getContext());
        cogwheel.setBackground(getActivity().getResources().getDrawable(imageId));
        RelativeLayout.LayoutParams lp_cogwheel = new RelativeLayout.LayoutParams(width, height);
        cogwheel.setLayoutParams(lp_cogwheel);
        cogwheel.setX(x);
        cogwheel.setY(y);
        container.addView(cogwheel);
        cogwheel.startAnimation(new RotateAnimationCogwheel(fromDegrees, toDegrees, pivotX, pivotY, duration, Animation.REVERSE, Animation.INFINITE));
    }

    //type 1 - помощь ии
    // 2 - оставить отзыв
    public void sendMessage(String textTop, String textBot, String textMail, int type) {
        isSend = true;
        mTimer.cancel();
        backgroundAnimationTimer.cancel();
        isTimerPlay = false;
        if (!isGameOver) {
            if (roundFinish.isPlaying()) {
                isRoundFinishPlay = true;
                roundFinish.pause();
            } else
                isRoundFinishPlay = false;
            if (roundStart.isPlaying()) {
                isRoundStartPlay = true;
                roundStart.pause();
            } else
                isRoundStartPlay = false;
            if (cooldownFinish.isPlaying()) {
                isCooldownFinishPlay = true;
                cooldownFinish.pause();
            } else
                isCooldownFinishPlay = false;
            if (leftWay != null) {
                leftWay.soundPause();
                centerWay.soundPause();
                rightWay.soundPause();
                baseAlly.soundPause();
            }
        }

        RelativeLayout rl_container = ((RelativeLayout) view.findViewById(R.id.rl_main_field));
        rl_container.removeAllViews();
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(rl_container.getWidth(), rl_container.getHeight());
        View rl_bg = new View(getActivity());
        rl_bg.setLayoutParams(lp);
        rl_bg.setBackground(getActivity().getResources().getDrawable(R.drawable.bg_city));
        rl_container.removeAllViews();
        //добавимка общий лайаут
        RelativeLayout rl_cogwheel = new RelativeLayout(getActivity());
        RelativeLayout.LayoutParams lp_cogwheel = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lp_cogwheel.addRule(RelativeLayout.CENTER_IN_PARENT);
        rl_cogwheel.setLayoutParams(lp_cogwheel);
        rl_container.addView(rl_cogwheel);
        //фон
        ImageView iv_bg = new AdjustableImageView(view.getContext());
        iv_bg.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.bg));
        iv_bg.setAdjustViewBounds(true);
        rl_cogwheel.addView(iv_bg);
        //колесо подлджка
        View v_cogwheel_0 = new View(view.getContext());
        v_cogwheel_0.setBackground(getActivity().getResources().getDrawable(R.drawable.cogwheel0));
        lp_cogwheel = new RelativeLayout.LayoutParams((int) (WIDTH_FIELD * 0.4739f), (int) (WIDTH_FIELD * 0.4739f));
        v_cogwheel_0.setLayoutParams(lp_cogwheel);
        v_cogwheel_0.setX(WIDTH_FIELD * 0.1358f);
        v_cogwheel_0.setY(WIDTH_FIELD * 0.62427f * 0.1921f);
        rl_cogwheel.addView(v_cogwheel_0);
        //колесо большое
        startSendAnimationCogwheel(rl_cogwheel, R.drawable.cogwheel1, (int) (WIDTH_FIELD * 0.6546f), (int) (WIDTH_FIELD * 0.6546f), WIDTH_FIELD * 0.38505f - (WIDTH_FIELD * 0.6546f) / 2, WIDTH_FIELD * 0.62427f * 0.5601f - (WIDTH_FIELD * 0.6546f) / 2, 0, 360, WIDTH_FIELD * 0.38505f, WIDTH_FIELD * 0.62427f * 0.5601f, 10000);
        //колесо малое фоновое левое
        startSendAnimationCogwheel(rl_cogwheel, R.drawable.cogwheel3, (int) (WIDTH_FIELD * 0.2456f), (int) (WIDTH_FIELD * 0.2456f), WIDTH_FIELD * 0.7687f - (WIDTH_FIELD * 0.2456f) / 2, WIDTH_FIELD * 0.62427f * 0.3611f - (WIDTH_FIELD * 0.2456f) / 2, 0, 360, WIDTH_FIELD * 0.7687f, WIDTH_FIELD * 0.62427f * 0.3611f, 20000);
        //колесо малое фоновое правое
        startSendAnimationCogwheel(rl_cogwheel, R.drawable.cogwheel3, (int) (WIDTH_FIELD * 0.2456f), (int) (WIDTH_FIELD * 0.2456f), WIDTH_FIELD * 0.8988f - (WIDTH_FIELD * 0.2456f) / 2, WIDTH_FIELD * 0.62427f * 0.2083f - (WIDTH_FIELD * 0.2456f) / 2, 0, -360, WIDTH_FIELD * 0.8988f, WIDTH_FIELD * 0.62427f * 0.2083f, 20000);
        //колесо малое основное
        startSendAnimationCogwheel(rl_cogwheel, R.drawable.cogwheel2, (int) (WIDTH_FIELD * 0.289f), (int) (WIDTH_FIELD * 0.289f), WIDTH_FIELD * 0.6950f - (WIDTH_FIELD * 0.289f) / 2, WIDTH_FIELD * 0.62427f * 0.1643f - (WIDTH_FIELD * 0.289f) / 2, 0, -360, WIDTH_FIELD * 0.6950f, WIDTH_FIELD * 0.62427f * 0.1643f, 10000);

        rl_container.addView(rl_bg);

        //искры вверх
        startSendAnimation(rl_container, R.drawable.fair3, (int) (WIDTH_FIELD * 0.6f), (int) (WIDTH_FIELD * 0.6f), RelativeLayout.CENTER_HORIZONTAL, 0, R.anim.translate_alpha_top);
        //искры вверх влево
        startSendAnimation(rl_container, R.drawable.fair2, (int) (WIDTH_FIELD * 0.6f), (int) (WIDTH_FIELD * 0.6f), RelativeLayout.ALIGN_PARENT_LEFT, 0, R.anim.translate_alpha_top_l);
        //искры вверх вправо
        startSendAnimation(rl_container, R.drawable.fair5, (int) (WIDTH_FIELD * 0.6f), (int) (WIDTH_FIELD * 0.6f), RelativeLayout.ALIGN_PARENT_RIGHT, 0, R.anim.translate_alpha_top_r);
        //искры вниз
        startSendAnimation(rl_container, R.drawable.fair4, (int) (WIDTH_FIELD * 0.6f), (int) (WIDTH_FIELD * 0.6f), RelativeLayout.CENTER_HORIZONTAL, HEIGHT_FIELD * 0.5f, R.anim.translate_alpha_bot);
        //искры вниз влево
        startSendAnimation(rl_container, R.drawable.fair1, (int) (WIDTH_FIELD * 0.6f), (int) (WIDTH_FIELD * 0.6f), RelativeLayout.ALIGN_PARENT_LEFT, HEIGHT_FIELD * 0.5f, R.anim.translate_alpha_bot_l);
        //искры вниз вправо
        startSendAnimation(rl_container, R.drawable.fair0, (int) (WIDTH_FIELD * 0.6f), (int) (WIDTH_FIELD * 0.6f), RelativeLayout.ALIGN_PARENT_RIGHT, HEIGHT_FIELD * 0.5f, R.anim.translate_alpha_bot_r);
        //перья вверх
        startSendAnimation(rl_container, R.drawable.feathers3, (int) (WIDTH_FIELD * 0.6f), (int) (WIDTH_FIELD * 0.6f), RelativeLayout.ALIGN_PARENT_RIGHT, 0, R.anim.translate_alpha_bot_r);
        //перья вверх влево
        startSendAnimation(rl_container, R.drawable.feathers2, (int) (WIDTH_FIELD * 0.6f), (int) (WIDTH_FIELD * 0.6f), RelativeLayout.ALIGN_PARENT_LEFT, 0, R.anim.translate_alpha_top_l);
        //перья вверх вправо
        startSendAnimation(rl_container, R.drawable.feathers0, (int) (WIDTH_FIELD * 0.6f), (int) (WIDTH_FIELD * 0.6f), RelativeLayout.CENTER_HORIZONTAL, 0, R.anim.translate_alpha_top);
        //перья вниз
        startSendAnimation(rl_container, R.drawable.feathers4, (int) (WIDTH_FIELD * 0.6f), (int) (WIDTH_FIELD * 0.6f), RelativeLayout.ALIGN_PARENT_RIGHT, HEIGHT_FIELD * 0.5f, R.anim.translate_alpha_top_r);
        //перья вниз влево
        startSendAnimation(rl_container, R.drawable.feathers1, (int) (WIDTH_FIELD * 0.6f), (int) (WIDTH_FIELD * 0.6f), RelativeLayout.CENTER_HORIZONTAL, HEIGHT_FIELD * 0.5f, R.anim.translate_alpha_bot);

        TextView tv_help_top = new TextView(getActivity());
        lp_cogwheel = new RelativeLayout.LayoutParams((int) (WIDTH_FIELD * 0.8f), ViewGroup.LayoutParams.WRAP_CONTENT);
        lp_cogwheel.addRule(RelativeLayout.CENTER_HORIZONTAL);
        tv_help_top.setLayoutParams(lp_cogwheel);
        tv_help_top.setTextColor(Color.WHITE);
        tv_help_top.setGravity(Gravity.CENTER_HORIZONTAL);
        tv_help_top.setText(textTop);
        tv_help_top.setTextSize(TypedValue.COMPLEX_UNIT_PX, HEIGHT_FIELD / 28);
        tv_help_top.setY(HEIGHT_FIELD / 10);
        tv_help_top.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), "9364.ttf"));

        rl_container.addView(tv_help_top);

        TextView tv_help_bot = new TextView(getActivity());
        lp_cogwheel = new RelativeLayout.LayoutParams((int) (WIDTH_FIELD * 0.8f), ViewGroup.LayoutParams.WRAP_CONTENT);
        lp_cogwheel.addRule(RelativeLayout.CENTER_HORIZONTAL);
        tv_help_bot.setLayoutParams(lp_cogwheel);
        tv_help_bot.setTextColor(Color.WHITE);
        tv_help_bot.setGravity(Gravity.CENTER_HORIZONTAL);
        tv_help_bot.setText(textBot);
        tv_help_bot.setTextSize(TypedValue.COMPLEX_UNIT_PX, HEIGHT_FIELD / 28);
        tv_help_bot.setY(HEIGHT_FIELD / 10 * 6.6f);
        tv_help_bot.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), "9364.ttf"));

        rl_container.addView(tv_help_bot);

        TextView tv_help_mail = new TextView(getActivity());
        lp_cogwheel = new RelativeLayout.LayoutParams((int) (WIDTH_FIELD * 0.8f), ViewGroup.LayoutParams.WRAP_CONTENT);
        lp_cogwheel.addRule(RelativeLayout.CENTER_HORIZONTAL);
        tv_help_mail.setLayoutParams(lp_cogwheel);
        tv_help_mail.setTextColor(Color.WHITE);
        tv_help_mail.setGravity(Gravity.CENTER_HORIZONTAL);
        Spanned textSpan = android.text.Html.fromHtml("<u>" + textMail + "</u>");
        tv_help_mail.setText(textSpan);
        tv_help_mail.setTextSize(TypedValue.COMPLEX_UNIT_PX, HEIGHT_FIELD / 30);
        tv_help_mail.setY(HEIGHT_FIELD / 10 * 8.6f);
        tv_help_mail.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), "9364.ttf"));

        rl_container.addView(tv_help_mail);

        if (type == 1) {
            tv_help_mail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    sp.edit().putBoolean("isHelp", true).apply();
                    Intent gmail = new Intent(Intent.ACTION_VIEW);
                    gmail.setClassName("com.google.android.gm", "com.google.android.gm.ComposeActivityGmail");
                    gmail.putExtra(Intent.EXTRA_EMAIL, new String[]{getString(R.string.help_mail)});
                    gmail.setData(Uri.parse(getString(R.string.help_mail)));
                    gmail.putExtra(Intent.EXTRA_SUBJECT, "help us");
                    gmail.setType("plain/text");
                    startActivity(gmail);
                    getActivity().onBackPressed();
                }
            });
        } else {
            tv_help_mail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final String appPackageName = getActivity().getPackageName();
                    try {
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                    } catch (android.content.ActivityNotFoundException anfe) {
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
                    }
                    getActivity().onBackPressed();
                }
            });
        }
    }

    public void soundsRelease() {
        if (leftWay != null) {
            leftWay.soundRelease();
            centerWay.soundRelease();
            rightWay.soundRelease();
        }

        cooldownFinish.release();
        roundStart.release();
        roundFinish.release();
        if (baseAlly != null)
            if (baseAlly.getSound() != null)
                baseAlly.getSound().release();
    }

    //--------------------> ANIMATIONS <--------------------------------

    public void setBackgroundAnimation(float verticalRoadX, float horizontalRoadY, float widthRoad) {
        HEIGHT_FIELD = view.getHeight();
        WIDTH_FIELD = view.getWidth();
        verticalWay = WIDTH_FIELD / 2 - HEIGHT_FIELD * verticalRoadX;
        horizontalWay = HEIGHT_FIELD * horizontalRoadY;
        sizeWay = HEIGHT_FIELD * widthRoad;

        backgroundAnimationTimer = new Timer();
        backgroundAnimationTimer.schedule(new CarsMovementTask(), 42, 42);

        if (HEIGHT_FIELD != 0) {
            //add clouds
            addAnimateView(R.drawable.anim_cloud1, R.id.layer_clouds, true, 0, new Random().nextInt((int) (HEIGHT_FIELD - HEIGHT_FIELD / 10)), HEIGHT_FIELD * 0.4f, 0.42f, 44000, 0, 0, 10000);
            addAnimateView(R.drawable.anim_cloud2, R.id.layer_clouds, true, 0, new Random().nextInt((int) (HEIGHT_FIELD - HEIGHT_FIELD / 10)), HEIGHT_FIELD * 0.35f, 0.57f, 46000, 1000, 0, 10000);
            addAnimateView(R.drawable.anim_cloud3, R.id.layer_clouds, true, 0, new Random().nextInt((int) (HEIGHT_FIELD - HEIGHT_FIELD / 10)), HEIGHT_FIELD * 0.3f, 0.6f, 45000, 16000, 0, 10000);
            addAnimateView(R.drawable.anim_cloud4, R.id.layer_clouds, true, 0, new Random().nextInt((int) (HEIGHT_FIELD - HEIGHT_FIELD / 10)), HEIGHT_FIELD * 0.4f, 0.3f, 43000, 21000, 0, 10000);
            addAnimateView(R.drawable.anim_cloud5, R.id.layer_clouds, true, 0, new Random().nextInt((int) (HEIGHT_FIELD - HEIGHT_FIELD / 10)), HEIGHT_FIELD * 0.45f, 0.66f, 47000, 21000, 0, 10000);
            //add clouds
            addAnimateView(R.drawable.anim_cloud1, R.id.layer_clouds, true, 0, new Random().nextInt((int) WIDTH_FIELD), HEIGHT_FIELD * 0.35f, 0.42f, 43000, -1, -1, 10000);
            addAnimateView(R.drawable.anim_cloud3, R.id.layer_clouds, true, 0, new Random().nextInt((int) WIDTH_FIELD), HEIGHT_FIELD * 0.45f, 0.6f, 44000, -2, -1, 10000);
            addAnimateView(R.drawable.anim_cloud5, R.id.layer_clouds, true, 0, new Random().nextInt((int) WIDTH_FIELD), HEIGHT_FIELD * 0.4f, 0.66f, 47000, 0, -1, 10000);
        } else Toast.makeText(getActivity(), "Error", Toast.LENGTH_LONG).show();
        horizontalRoad = new ArrayList<>();
        verticalRoad = new ArrayList<>();

        //add tracks
        horizontalRoad.add(new Auto(1, 0, addAuto(WIDTH_FIELD, horizontalWay - sizeWay / 2, false, true), true));
        horizontalRoad.add(new Auto(-1, 0, addAuto(-1, horizontalWay + sizeWay / 2, false, true), true));
        verticalRoad.add(new Auto(0, 1, addAuto(verticalWay + sizeWay / 2, HEIGHT_FIELD, true, true), true));
        verticalRoad.add(new Auto(0, -1, addAuto(verticalWay - sizeWay / 2, -1, true, true), true));

        //add cars
        for (int i = 0; i < 3; i++) {
            int randomize = (int) (WIDTH_FIELD / 4 - sizeWay * 2);
            if (randomize <= 0) randomize = 1;
            horizontalRoad.add(new Auto(1, 0, addAuto(new Random().nextInt(randomize) + WIDTH_FIELD / 4 * i, horizontalWay - sizeWay / 2, false, false), false));
            horizontalRoad.add(new Auto(-1, 0, addAuto(new Random().nextInt(randomize) + WIDTH_FIELD / 4 * i, horizontalWay + sizeWay / 2, false, false), false));
        }
        for (int i = 0; i < 4; i++) {
            int randomize = (int) (HEIGHT_FIELD / 4 - sizeWay * 2);
            if (randomize <= 0) randomize = 1;
            verticalRoad.add(new Auto(0, 1, addAuto(verticalWay + sizeWay / 2, new Random().nextInt(randomize) + HEIGHT_FIELD / 5 * i, true, false), false));
            verticalRoad.add(new Auto(0, -1, addAuto(verticalWay - sizeWay / 2, new Random().nextInt(randomize) + HEIGHT_FIELD / 5 * i, true, false), false));
        }

        //add trains
        addAnimateView(R.drawable.anim_train, R.id.layer_second, false, 0, HEIGHT_FIELD * 0.036f, HEIGHT_FIELD * 0.937f, 0.027f, 18000, new Random().nextInt(10000), 20000, 30000);
        addAnimateView(R.drawable.anim_train, R.id.layer_second, true, 0, HEIGHT_FIELD * 0.02f, HEIGHT_FIELD * 0.937f, 0.027f, 18000, new Random().nextInt(20000), 20000, 30000);
    }

    public ImageView addAuto(float x, float y, boolean isVertical, boolean isTruck) {
        ImageView car = new ImageView(getActivity());
        float size = isTruck ? sizeWay * 1.2f : sizeWay * 0.8f;
        int width = (int) size, height = (int) size;
        if (isVertical)
            height = (int) (size * 1.75f);
        else
            width = (int) (size * 1.75f);
        car.setLayoutParams(new ViewGroup.LayoutParams(width, height));
        ((RelativeLayout) view.findViewById(R.id.layer_first)).addView(car);
        car.setX(x - width / 2);
        car.setY(y - height / 2);
        return car;
    }

    public void addAnimateView(int drawableId, int layer, boolean isStart, float x, float y, float size, float secondSize,
                               int durationWay, int durationStart, int durationCooldown, int durationRandom) {

        //add view
        ImageView imageView;
        imageView = new ImageView(getActivity());
        imageView.setImageResource(drawableId);
        imageView.setAdjustViewBounds(true);
        ((RelativeLayout) view.findViewById(layer)).addView(imageView);
        imageView.getLayoutParams().width = (int) size;
        imageView.getLayoutParams().height = (int) (size * secondSize);

        TranslateAnimation translateAnimation;
        if (x == 0) {
            imageView.setY(y);
            if (isStart) {
                if (durationCooldown >= 0)
                    imageView.setX(-size);
                else
                    imageView.setX(new Random().nextInt((int) WIDTH_FIELD / 6) + WIDTH_FIELD / 2 + WIDTH_FIELD / 6 * durationStart);
                translateAnimation = new TranslateAnimation(0, WIDTH_FIELD + size, 0, 0);
            } else {
                if (durationCooldown >= 0)
                    imageView.setX(WIDTH_FIELD);
                else
                    imageView.setX(new Random().nextInt((int) WIDTH_FIELD));
                translateAnimation = new TranslateAnimation(0, -size - WIDTH_FIELD, 0, 0);
            }
        } else {
            imageView.setX(x);
            if (isStart) {
                imageView.setY(-size);
                translateAnimation = new TranslateAnimation(0, 0, 0, HEIGHT_FIELD + size);
            } else {
                imageView.setY(HEIGHT_FIELD);
                translateAnimation = new TranslateAnimation(0, 0, 0, -HEIGHT_FIELD - size);
            }
        }
        translateAnimation.setInterpolator(new LinearInterpolator());
        translateAnimation.setDuration(durationWay);
        AnimateView anim = new AnimateView(imageView, translateAnimation, durationStart, durationCooldown, durationRandom, HEIGHT_FIELD);
        anim.start();
    }

    class CarsMovementTask extends TimerTask {
        @Override
        public void run() {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (isTimerPlay) {
                        boolean isLight = false;
                        boolean isMove;
                        int speed = 1;
                        for (Auto car : verticalRoad) {
                            if (car.getView().getY() > HEIGHT_FIELD & car.getSpeedY() > 0) {
                                car.getView().setY(-1 * new Random().nextInt(300));
                                car.resetAuto();
                            } else if (car.getView().getY() + car.getView().getHeight() < 0 & car.getSpeedY() < 0) {
                                car.getView().setY(HEIGHT_FIELD + new Random().nextInt(300));
                                car.resetAuto();
                            }
                            isMove = true;
                            for (Auto search : verticalRoad) {
                                if (search.getView().getY() != car.getView().getY() & search.getSpeedY() == car.getSpeedY() &
                                        Math.abs(car.getView().getY() + car.getView().getHeight() / 2 - search.getView().getY() - search.getView().getHeight() / 2) < car.getView().getHeight() + search.getView().getHeight())
                                    if ((car.getView().getY() > search.getView().getY() & car.getSpeedY() > 0)
                                            | (car.getView().getY() < search.getView().getY() & car.getSpeedY() < 0))
                                        isMove = false;
                                if (search.getView().getY() + search.getView().getHeight() > horizontalWay - sizeWay * 5
                                        & search.getView().getY() < horizontalWay + sizeWay
                                        & search.getSpeedY() > 0)
                                    isLight = true;
                                if (search.getView().getY() < horizontalWay + sizeWay * 5
                                        & search.getView().getY() + search.getView().getHeight() > horizontalWay - sizeWay
                                        & search.getSpeedY() < 0)
                                    isLight = true;
                            }
                            if (isMove)
                                car.getView().setY(car.getView().getY() + speed * car.getSpeedY());
                        }
                        for (Auto car : horizontalRoad) {
                            if (car.isRun()) {
                                if (car.getView().getX() > WIDTH_FIELD & car.getSpeedX() > 0) {
                                    car.getView().setX(-1 * new Random().nextInt(300));
                                    car.resetAuto();
                                } else if (car.getView().getX() + car.getView().getWidth() < 0 & car.getSpeedX() < 0) {
                                    car.getView().setX(WIDTH_FIELD + new Random().nextInt(300));
                                    car.resetAuto();
                                }
                                for (Auto search : horizontalRoad) {
                                    if (search.getView().getX() != car.getView().getX() & search.getSpeedX() == car.getSpeedX() &
                                            Math.abs(car.getView().getX() + car.getView().getWidth() / 2 - search.getView().getX() - search.getView().getWidth() / 2) < car.getView().getWidth() + search.getView().getWidth())
                                        if ((car.getView().getX() < search.getView().getX() & car.getSpeedX() > 0)
                                                | (car.getView().getX() > search.getView().getX() & car.getSpeedX() < 0))
                                            car.setRun(false, sizeWay / 2);
                                }
                                if (car.getSpeedX() > 0 & car.getView().getX() + car.getView().getWidth() + speed * car.getSpeedX() >= verticalWay - sizeWay * 3
                                        & car.getView().getX() + car.getView().getWidth() <= verticalWay - sizeWay & isLight)
                                    car.setRun(false, sizeWay / 2);
                                if (car.getSpeedX() < 0 & car.getView().getX() + speed * car.getSpeedX() <= verticalWay + sizeWay * 3
                                        & car.getView().getX() >= verticalWay + sizeWay & isLight)
                                    car.setRun(false, sizeWay / 2);
                            } else {
                                if (!isLight) car.setRun(true, sizeWay / 2);
                            }
                            if (car.isRun() & car.getView().isEnabled())
                                car.getView().setX(car.getView().getX() + speed * car.getSpeedX());
                        }
                    }
                }
            });
        }

    }

    //------------------->Start game <-----------------------

    public void randomMode() {
        //случайный мод
        if (mode > 4) {
            int chanse = new Random().nextInt(100);
            if (chanse < 11) {
                mode = new Random().nextInt(Constants.LAST_MODE_AI);
            }
        }
    }

    public void delayStart() {
        //запуск не сразу а с задержкой
        if (mode > 4) {
            changeTime = new Random().nextInt(80);
            tics = PLANC_MAX - changeTime;
        }
    }

    //------------------->Tutorial <-----------------------
    public void initializeTutorialDialog() {
        layout = (RelativeLayout) getActivity().findViewById(R.id.tutorial);
        layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tutorialClick();
            }
        });
        shadowTop = new View(getActivity());
        shadowTop.setBackgroundColor(Color.parseColor("#ee000000"));
        shadowTop.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        layout.addView(shadowTop);
        shadowBottom = new View(getActivity());
        shadowBottom.setBackgroundColor(Color.parseColor("#ee000000"));
        shadowBottom.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        layout.addView(shadowBottom);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, layout.getHeight() / Constants.TUTORIAL_ARROW_SIZE);
        params.addRule(RelativeLayout.CENTER_HORIZONTAL);
        arrow = new AdjustableImageView(getActivity());
        arrow.setImageResource(R.drawable.tutorial_arrow);
        arrow.setAdjustViewBounds(true);
        arrow.setLayoutParams(params);
        layout.addView(arrow);
        text = new TextView(getActivity());
        text.setGravity(Gravity.CENTER);
        text.setTextSize((int) (getActivity().findViewById(R.id.field).getHeight() * 0.027f));
        text.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), "9364.ttf"));
        text.setTextColor(Color.parseColor("#f9f9f9"));
        layout.addView(text);
        ((RelativeLayout.LayoutParams) text.getLayoutParams()).addRule(RelativeLayout.CENTER_HORIZONTAL);
    }

    public void updateTutorialDialog() {
        layout.setVisibility(View.VISIBLE);
        isTutorialOpened = true;
        lastDialog++;
        switch (lastDialog) {
            case 1:
                top = (int) (centerWay.getNearestEnemyY() * getActivity().findViewById(R.id.field).getHeight() - getActivity().findViewById(R.id.field).getWidth() / 2 * 1.2f / 2);
                bottom = top + (int) (getActivity().findViewById(R.id.field).getWidth() / 2 * 1.2f) / 4 * 3;
                isUnderArrow = true;
                text.setText(getResources().getString(R.string.tutorial_dialog1));
                break;
            case 2:
                top = (int) (getActivity().findViewById(R.id.field).getHeight() * 0.95f);
                bottom = (int) (getActivity().findViewById(R.id.field).getHeight() * 1.05f);
                isUnderArrow = false;
                text.setText(getResources().getString(R.string.tutorial_dialog2));
                break;
            case 3:
                top = getActivity().findViewById(R.id.field).getHeight();
                bottom = layout.getHeight();
                isUnderArrow = false;
                text.setText(getResources().getString(R.string.tutorial_dialog3));
                break;
            case 4:
                top = 0;
                bottom = (int) (getActivity().findViewById(R.id.field).getHeight() * 0.05f);
                isUnderArrow = true;
                text.setText(getResources().getString(R.string.tutorial_dialog4));
                break;
            case 5:
                top = (int) (getActivity().findViewById(R.id.field).getHeight() * 0.6f);
                bottom = (int) (getActivity().findViewById(R.id.field).getHeight() * 1.05f);
                isUnderArrow = false;
                text.setText(getResources().getString(R.string.tutorial_dialog5));
                break;
        }
        text.post(new Runnable() {
            @Override
            public void run() {
                shadowTop.setY(top - layout.getHeight());
                shadowBottom.setY(bottom);
                if (isUnderArrow) {
                    arrow.setY(bottom - (bottom - top) / 5);
                    arrow.setRotation(0);
                    text.setY(arrow.getY() + layout.getHeight() / Constants.TUTORIAL_ARROW_SIZE);
                } else {
                    arrow.setY(top + (bottom - top) / 5 - layout.getHeight() / Constants.TUTORIAL_ARROW_SIZE);
                    arrow.setRotation(180);
                    text.setY(arrow.getY() - text.getHeight());
                }
            }
        });
    }

    public void tutorialClick() {
        if (lastDialog > 2) {
            layout.setVisibility(View.GONE);
            view.findViewById(R.id.pause).callOnClick();
            isTutorialOpened = false;
        } else {
            updateTutorialDialog();
        }
    }
}