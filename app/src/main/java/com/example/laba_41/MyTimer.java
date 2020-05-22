package com.example.laba_41;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.os.CountDownTimer;
import android.widget.RemoteViews;

public class MyTimer extends CountDownTimer {
    Context context;
    AppWidgetManager appWidgetManager;
    public MyTimer(long startTime, long interval, Context context, AppWidgetManager appWidgetManager) {
        super(startTime, interval);
        this.context = context;
        this.appWidgetManager = appWidgetManager;
    }

    @Override
    public void onFinish() {
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(),
                R.layout.activity_main);
        remoteViews.setTextViewText(R.id.date_str, "data ne vybrana");
        ComponentName appWidget = new ComponentName(context, MainActivity.class);
        appWidgetManager.updateAppWidget(appWidget, remoteViews);
    }

    @Override
    public void onTick(long millisUntilFinished) {
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(),
                R.layout.activity_main);
        long res = millisUntilFinished / MainActivity.MDAY;
        if (millisUntilFinished > MainActivity.MDAY) {
            remoteViews.setTextViewText(R.id.days_num, Long.toString(res));
        } else {
            remoteViews.setTextViewText(R.id.days_num, "0");
        }
        ComponentName appWidget = new ComponentName(context, MainActivity.class);
        appWidgetManager.updateAppWidget(appWidget, remoteViews);

    }
}