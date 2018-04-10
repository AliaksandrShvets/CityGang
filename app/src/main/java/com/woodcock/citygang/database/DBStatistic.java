package com.woodcock.citygang.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by user on 03.10.2015.
 */
public class DBStatistic extends SQLiteOpenHelper {
    public DBStatistic(Context context) {
        super(context, "info", null, 1);
    }

    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table mystat ("
                + "date text,"
                + "win integer,"
                + "round integer,"
                + "premium integer"
                + ");");
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }
}