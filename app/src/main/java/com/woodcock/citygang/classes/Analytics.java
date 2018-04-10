package com.woodcock.citygang.classes;

import android.app.Activity;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.woodcock.citygang.MyApplication;

/**
 * Created by user on 23.09.2016.
 */
public class Analytics {

    private Tracker mTracker;

    public Analytics(Activity a) {
        MyApplication application = (MyApplication) a.getApplication();
        mTracker = application.getTracker(MyApplication.TrackerName.APP_TRACKER);
        mTracker.enableAdvertisingIdCollection(true);
    }

    public void Send(String screenName)
    {
        mTracker.setScreenName(screenName);
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());
    }

    public void Send(String category, String action, String label)
    {
        mTracker.send(new HitBuilders.EventBuilder()
                .setCategory(category)
                .setAction(action)
                .setLabel(label)
                .build()
        );
    }
}
