package com.woodcock.citygang;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;

import java.util.Timer;
import java.util.TimerTask;

public class NotificationService extends Service {

    private Timer timer;
    private MyTimerTask timerTask;
    private int tics, ticsMax;
    SharedPreferences sp;

    public NotificationService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        sp=getSharedPreferences("mysettings",MODE_PRIVATE);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        tics=0;
        int time[]=getResources().getIntArray(R.array.time_notify);
        ticsMax=time[sp.getInt("sendNotification",0)];
        timer=new Timer();
        timerTask=new MyTimerTask();
        timer.schedule(timerTask,1000,1000);
        return super.onStartCommand(intent, flags, startId);
    }



    private void sendNotification(int notificationId, String ticker, String title, String text)
    {
        Intent notificationIntent = new Intent(this, MainUI.class);
        PendingIntent contentIntent = PendingIntent.getActivity(this,
                0, notificationIntent,
                PendingIntent.FLAG_CANCEL_CURRENT);

        Resources res = this.getResources();
        Notification.Builder builder = new Notification.Builder(this);

        builder.setContentIntent(contentIntent)
                .setSmallIcon(R.drawable.icon_mex) // большая картинка
                .setTicker(ticker)
                .setWhen(System.currentTimeMillis())
                .setAutoCancel(true)
                .setDefaults(Notification.DEFAULT_ALL)
                .setContentTitle(title) //Заголовок
                .setContentText(text); // Текст уведомления

        Notification notification = builder.build();

        NotificationManager notificationManager = (NotificationManager) this
                .getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(notificationId, notification);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    private class MyTimerTask extends TimerTask
    {
        @Override
        public void run() {
            tics++;
            if(tics==ticsMax) {
                int count=sp.getInt("sendNotification",0);
                count++;
                sp.edit().putInt("sendNotification",count).apply();
                sendNotification(101,"Город Войны: бой продложается","Город Войны","Бой продложается!");
                stopSelf();
            }
        }
    }
}
