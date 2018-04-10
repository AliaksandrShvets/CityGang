package com.woodcock.citygang.classes;

import android.app.Activity;
import android.content.SharedPreferences;
import android.util.Log;

import com.woodcock.citygang.MainUI;

import java.util.Date;

/**
 * Created by user on 24.07.2016.
 */
public class TimeHelper {

    private String[] months;
    private TimeViewer timeViewer;
    private SharedPreferences sPref;
    private int hour_serv, minute_serv, second_serv;
    private static final String APP_PREFERENCES = "mysettings";
    private Activity a;

    public TimeHelper(Activity activity) {
        a = activity;
        months = new String[]{"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
        timeViewer = new TimeViewer();
        try {
            timeViewer.run();
        } catch (Exception e) {
            String c = e.getMessage();
        }

        try {
            sPref = a.getSharedPreferences(APP_PREFERENCES, a.MODE_PRIVATE);
        } catch (NullPointerException e) {
            Log.d("time", " " + e.getMessage());
        }
    }

    public void updateTimeService() {
        try {
            timeViewer.run();
        } catch (Exception e) {
            String c = e.getMessage();
        }
    }

    public boolean isTime() {
        SharedPreferences.Editor editor = sPref.edit();
        Date date_server;
        Date date_now;

        String[] date = timeViewer.Date();
        String[] time = timeViewer.Time();
        date_now = new Date(Integer.valueOf(date[2]), getMonth(date[1]), Integer.valueOf(date[0]));

        String[] date_serv = sPref.getString("date", "").split(" ");
        hour_serv = sPref.getInt("hour", 0);
        minute_serv = sPref.getInt("minute", 0);
        second_serv = sPref.getInt("second", 0);
        if (date_serv.length < 2) {
            String dt = date_now.getDate() + " " + date_now.getMonth() + " " + date_now.getYear();
            editor.putString("date", dt);
            editor.putInt("hour", Integer.valueOf(time[0]));
            editor.putInt("minute", Integer.valueOf(time[1]));
            editor.putInt("second", Integer.valueOf(time[2]));
            editor.apply();
            return true;
        }
        date_server = new Date(Integer.valueOf(date_serv[2]), Integer.valueOf(date_serv[1]), Integer.valueOf(date_serv[0]));

        if (!equalsDate(date_now, date_server)) {
            String dt = date_now.getDate() + " " + date_now.getMonth() + " " + date_now.getYear();
            editor.putString("date", dt);
            editor.putInt("hour", Integer.valueOf(time[0]));
            editor.putInt("minute", Integer.valueOf(time[1]));
            editor.putInt("second", Integer.valueOf(time[2]));
            editor.apply();
            return true;
        } else {
            if (hour_serv < Integer.valueOf(time[0])) {
                editor.putInt("hour", Integer.valueOf(time[0]));
                editor.putInt("minute", Integer.valueOf(time[1]));
                editor.putInt("second", Integer.valueOf(time[2]));
                editor.apply();
                return true;
            } else
                return false;
        }

    }

    public int countSecond(int second) {
        try {
            SharedPreferences.Editor editor = sPref.edit();
            Date date_server;
            Date date_now;

            String[] date = timeViewer.Date();
            String[] time = timeViewer.Time();

            date_now = new Date(Integer.valueOf(date[2]), getMonth(date[1]), Integer.valueOf(date[0]));

            String[] date_serv = sPref.getString("date", "").split(" ");
            hour_serv = sPref.getInt("hour", 0);
            minute_serv = sPref.getInt("minute", 0);
            second_serv = sPref.getInt("second", 0);
            if (date_serv.length < 2) {
                String dt = date_now.getDate() + " " + date_now.getMonth() + " " + date_now.getYear();
                editor.putString("date", dt);
                editor.putInt("hour", Integer.valueOf(time[0]));
                editor.putInt("minute", Integer.valueOf(time[1]));
                editor.putInt("second", Integer.valueOf(time[2]));
                editor.apply();
                return 0;
            }
            date_server = new Date(Integer.valueOf(date_serv[2]), Integer.valueOf(date_serv[1]), Integer.valueOf(date_serv[0]));
            if (!equalsDate(date_now, date_server)) {
                String dt = date_now.getDate() + " " + date_now.getMonth() + " " + date_now.getYear();
                editor.putString("date", dt);
                editor.putInt("hour", Integer.valueOf(time[0]));
                editor.putInt("minute", Integer.valueOf(time[1]));
                editor.putInt("second", Integer.valueOf(time[2]));
                editor.apply();
                return 0;
            } else {
                if (hour_serv < Integer.valueOf(time[0])) {
                    editor.putInt("hour", Integer.valueOf(time[0]));
                    editor.putInt("minute", Integer.valueOf(time[1]));
                    editor.putInt("second", Integer.valueOf(time[2]));
                    editor.apply();
                    return 0;
                } else {
                    int second_now = Integer.valueOf(time[2]) + Integer.valueOf(time[1]) * 60;
                    return 3600 - second_now;
                }

//            second_serv=second_serv+minute_serv*60+hour_serv*60*60;
//            int second_now=Integer.valueOf(time[2])+Integer.valueOf(time[1])*60+Integer.valueOf(time[0])*60*60;
//            if(second_now-second_serv>second) {
//                editor.putInt("hour",Integer.valueOf(time[0]));
//                editor.putInt("minute",Integer.valueOf(time[1]));
//                editor.putInt("second",Integer.valueOf(time[2]));
//                editor.apply();
//                return 3600;
//            }
//            else
//                return second_now-second_serv;

            }
        } catch (NumberFormatException e) {
            Log.d("error", " " + e.getMessage());
        }
        return 3598;
    }

    public int countSecond() {
        SharedPreferences.Editor editor = sPref.edit();
        Date date_server;
        Date date_now;

        String[] date = timeViewer.Date();
        String[] time = timeViewer.Time();
        date_now = new Date(Integer.valueOf(date[2]), getMonth(date[1]), Integer.valueOf(date[0]));

        String[] date_serv = sPref.getString("date", "").split(" ");
        hour_serv = sPref.getInt("hour", 0);
        minute_serv = sPref.getInt("minute", 0);
        second_serv = sPref.getInt("second", 0);
        if (date_serv.length < 2) {
            return 0;
        }
        date_server = new Date(Integer.valueOf(date_serv[2]), Integer.valueOf(date_serv[1]), Integer.valueOf(date_serv[0]));
        if (!equalsDate(date_now, date_server)) {
            return 0;
        } else {
            if (hour_serv < Integer.valueOf(time[0])) {
                return 0;
            } else {
                int second_now = Integer.valueOf(time[2]) + Integer.valueOf(time[1]) * 60;
                return 3600 - second_now;
            }
        }
    }

    private int getMonth(String month) {
        for (int i = 0; i < 12; i++) {
            if (months[i].equals(month))
                return i;
        }
        return 0;
    }

    private boolean equalsDate(Date d1, Date d2) {
        if (d1.getYear() != d2.getYear())
            return false;
        if (d1.getMonth() != d2.getMonth())
            return false;
        return (d1.getDate() == d2.getDate());

    }

}
