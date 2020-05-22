package com.example.laba_41;
import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.ComponentName;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import android.widget.RemoteViews;

import android.content.SharedPreferences;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;



public class MainActivity extends AppWidgetProvider {

    final static String LOG_TAG = "myLogs";
    static String SYNC_CLICKED = "Widget_Clicked";
    static SimpleDateFormat dateFormat;
    static Date date;
    static String CHANNEL_ID = "ll";
    final static long MDAY = 24L * 60L * 60L * 1000L;

    @Override
    public void onEnabled(Context context) {
        super.onEnabled(context);
        createNotCh(context);
        Log.d(LOG_TAG, "onEnabled");
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager,
                         int[] appWidgetIds) {
        super.onUpdate(context, appWidgetManager, appWidgetIds);
        dateFormat = new SimpleDateFormat("dd.MM.yyyy");
        date = new Date();
        SharedPreferences sp = context.getSharedPreferences(
                DatePickerFragment.WIDGET_PREF, Context.MODE_PRIVATE);
        for (int id : appWidgetIds) {
            updateWidget(context, appWidgetManager, sp, id);
        }
        Log.d(LOG_TAG, "onUpdate " + Arrays.toString(appWidgetIds));
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (SYNC_CLICKED.equals(intent.getAction())) {
            Intent i = new Intent(context, MyFragmentActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(i);
        }
        super.onReceive(context, intent);
    }

    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        super.onDeleted(context, appWidgetIds);
        Log.d(LOG_TAG, "onDeleted " + Arrays.toString(appWidgetIds));
    }

    @Override
    public void onDisabled(Context context) {
        super.onDisabled(context);
        Log.d(LOG_TAG, "onDisabled");
    }

    protected static PendingIntent getPendingSelfIntent(Context context, String action) {
        Intent intent = new Intent(context, MainActivity.class);
        intent.setAction(action);
        return PendingIntent.getBroadcast(context, 0, intent, 0);
    }

    static void updateWidget(Context context, AppWidgetManager appWidgetManager,
                             SharedPreferences sp, int widgetID) {
        Log.d(LOG_TAG, "updateWidget " + widgetID);
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(),
                R.layout.activity_main);
        ComponentName appWidget = new ComponentName(context, MainActivity.class);
        String widgetText = sp.getString(DatePickerFragment.WIDGET_TEXT_DATE + widgetID, null);
        if (widgetText != null) {
            remoteViews.setTextViewText(R.id.date_str, widgetText);
            try {
                date = dateFormat.parse(widgetText);
            } catch (Exception e) {
            }
            Intent intent = new Intent(context, MyBroadcast.class);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, 0);
            AlarmManager alarmManager =  (AlarmManager) context.getSystemService(context.ALARM_SERVICE);
            date.setHours(9);
            date.setMinutes(0);
            date.setSeconds(0);
            alarmManager.set(AlarmManager.RTC_WAKEUP, date.getTime(), pendingIntent);
            date.setHours(0);
            long res = date.getTime() - System.currentTimeMillis();
            remoteViews.setTextViewText(R.id.days_num, Long.toString(res  / (24L * 60L * 60L * 1000L)));
            MyTimer t = new MyTimer(res, MDAY, context,
                    appWidgetManager);
            t.start();
        } else {
            remoteViews.setTextViewText(R.id.date_str, "data ne vybrana");
            remoteViews.setOnClickPendingIntent(R.id.widget_place,
                    getPendingSelfIntent(context, SYNC_CLICKED));
        }
        appWidgetManager.updateAppWidget(appWidget, remoteViews);
    }

    private void createNotCh(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "LemubitReminderChannel";
            String desc = "Channel for Lemubit Reminder";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(desc);

            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
}
